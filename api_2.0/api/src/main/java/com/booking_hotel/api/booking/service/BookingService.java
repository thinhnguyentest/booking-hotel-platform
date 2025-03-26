package com.booking_hotel.api.booking.service;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.hotel.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<BookingResponse> getBookingById(Long id);
    BookingResponse createBooking(Booking Booking, String token, Long roomId);
    List<BookingResponse> getAllBookings();
    BookingResponse updateBooking(Long id, Booking updatedBooking);
    void deleteBooking(Long id);
    List<BookingResponse> searchBookings(Specification<Booking> specification);
    void sendDailyBookingReport();
}
