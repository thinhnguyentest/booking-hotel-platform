package com.booking_hotel.api.booking.controller;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.service.BookingService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private Booking booking;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        booking = new Booking(); // Khởi tạo đối tượng Booking
        bookingResponse = BookingResponse.builder().build(); // Khởi tạo đối tượng BookingResponse
    }

    @Test
    void testGetBookingById() {
        Long bookingId = 1L;
        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(bookingResponse));

        ResponseEntity<BookingResponse> response = bookingController.getBookingById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingResponse, response.getBody());
    }

    @Test
    void testGetBookingById_NotFound() {
        Long bookingId = 1L;
        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.empty());

        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            bookingController.getBookingById(bookingId);
        });

        assertEquals(MessageUtils.NOT_FOUND_BOOKING_MESSAGE, exception.getMessage());
    }

    @Test
    void testGetBookingsByUserId() {
        Long userId = 1L;
        List<BookingResponse> bookingResponses = Collections.singletonList(bookingResponse);
        when(bookingService.getBookingsByUserId(userId)).thenReturn(bookingResponses);

        ResponseEntity<List<BookingResponse>> response = bookingController.getBookingsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testCreateBooking() {
        Long roomId = 1L;
        String accessToken = "token";
        when(bookingService.createBooking(any(Booking.class), eq(accessToken), eq(roomId)))
                .thenReturn(bookingResponse);

        ResponseEntity<BookingResponse> response = bookingController.createBooking(booking, accessToken, roomId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookingResponse, response.getBody());
    }

    @Test
    void testUpdateBooking() {
        Long bookingId = 1L;
        when(bookingService.updateBooking(eq(bookingId), any(Booking.class))).thenReturn(bookingResponse);

        ResponseEntity<BookingResponse> response = bookingController.updateBooking(bookingId, booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingResponse, response.getBody());
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;
        doNothing().when(bookingService).deleteBooking(bookingId);

        ResponseEntity<?> response = bookingController.deleteBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking deleted successfully", response.getBody());
    }

    @Test
    void testGetAllBookings() {
        List<BookingResponse> bookingResponses = Collections.singletonList(bookingResponse);
        when(bookingService.getAllBookings()).thenReturn(bookingResponses);

        ResponseEntity<List<BookingResponse>> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchBookings() {
        // Thêm mã kiểm tra cho phương thức searchBookings nếu cần thiết
    }

    @Test
    void testSendDailyBookingReport() {
        doNothing().when(bookingService).sendDailyBookingReport();

        ResponseEntity<Void> response = bookingController.sendDailyBookingReport();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
