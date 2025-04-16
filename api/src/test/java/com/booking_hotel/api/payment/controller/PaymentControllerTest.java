package com.booking_hotel.api.payment.controller;

import com.booking_hotel.api.payment.dto.StripeResponse;
import com.booking_hotel.api.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleWebhook() {
        String payload = "{\"id\":\"evt_test\"}";
        String sigHeader = "test_signature";

        // Giả lập hành vi của paymentService
        when(paymentService.handleStripeWebhook(payload, sigHeader)).thenReturn(ResponseEntity.ok("Webhook received"));

        // Gọi phương thức
        ResponseEntity<String> response = paymentController.handleWebhook(payload, sigHeader);

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Webhook received", response.getBody());
        verify(paymentService).handleStripeWebhook(payload, sigHeader);
    }

    @Test
    void testCheckoutProducts() {
        Long bookingId = 1L;
        String accessToken = "test_access_token";
        StripeResponse stripeResponse = new StripeResponse(); // Giả lập đối tượng StripeResponse

        // Giả lập hành vi của paymentService
        when(paymentService.checkoutBooking(bookingId, accessToken)).thenReturn(stripeResponse);

        // Gọi phương thức
        ResponseEntity<StripeResponse> response = paymentController.checkoutProducts(bookingId, accessToken);

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stripeResponse, response.getBody());
        verify(paymentService).checkoutBooking(bookingId, accessToken);
    }
}
