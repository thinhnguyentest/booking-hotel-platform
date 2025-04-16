package com.booking_hotel.api.payment.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.mail.EmailService;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.payment.dto.StripeResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setTotalPrice(100.0);
        booking.setStatus("PENDING");
        User user = new User();
        user.setEmail("test@example.com");
        booking.setUser(user);
    }

    @Test
    void testCheckoutBooking_UserNotFound() {
        String token = "invalid_token";
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> {
            paymentService.checkoutBooking(1L, token);
        });
    }

    @Test
    void testCheckoutBooking_BookingNotFound() {
        String token = "valid_token";
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> {
            paymentService.checkoutBooking(1L, token);
        });
    }

    @Test
    void testCheckoutBooking_Success() throws StripeException {
        String token = "valid_token";
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Giả lập Stripe SessionCreateParams
        SessionCreateParams params = mock(SessionCreateParams.class);
        when(Session.create(any(SessionCreateParams.class))).thenReturn(mock(Session.class));

        // Giả lập Stripe Session
        Session session = mock(Session.class);
        when(session.getId()).thenReturn("session_id");
        when(session.getUrl()).thenReturn("http://example.com");
        when(Session.create(any(SessionCreateParams.class))).thenReturn(session);

        StripeResponse response = paymentService.checkoutBooking(1L, token);

        assertEquals("session_id", response.getSessionId());
        assertEquals("http://example.com", response.getSessionUrl());
    }


    @Test
    void testHandleStripeWebhook_InvalidPayload() {
        ResponseEntity<String> response = paymentService.handleStripeWebhook("invalid_payload", "signature");
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testConfirmPayment_BookingNotFound() throws StripeException {
        Event event = mock(Event.class);
        when(event.getData()).thenReturn(null);
        when(event.getType()).thenReturn("checkout.session.completed");

        assertThrows(ElementNotFoundException.class, () -> {
            paymentService.confirmPayment(event);
        });
    }

    @Test
    void testCancelPayment_BookingNotFound() throws StripeException {
        Event event = mock(Event.class);
        when(event.getData()).thenReturn(null);
        when(event.getType()).thenReturn("checkout.session.expired");

        assertThrows(ElementNotFoundException.class, () -> {
            paymentService.cancelPayment(event);
        });
    }
}
