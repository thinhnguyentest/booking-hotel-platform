package com.booking_hotel.api.booking.controller;

import com.booking_hotel.api.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.service.specifications.BookingSpecifications;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        Optional<BookingResponse> bookingOptional = bookingService.getBookingById(id);
        if (bookingOptional.isEmpty()) {
            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_BOOKING_MESSAGE);
        }
        return new ResponseEntity<>(bookingOptional.get(), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody Booking booking, @RequestHeader("Authorization") String accessToken, @RequestParam Long roomId) {
        String token = accessToken.substring(7);
        return new ResponseEntity<>(bookingService.createBooking(booking, token, roomId), HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        return new ResponseEntity<>(bookingService.updateBooking(id, updatedBooking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookingResponse>> searchBookings(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) ZonedDateTime checkInDate,
            @RequestParam(required = false) ZonedDateTime checkOutDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        Specification<Booking> specification = Specification.where(null);
        if (userId != null) {
            specification = specification.and(BookingSpecifications.hasCustomerId(userId));
        }
        if (roomId != null) {
            specification = specification.and(BookingSpecifications.hasRoomId(roomId));
        }
        if (checkInDate != null) {
            specification = specification.and(BookingSpecifications.hasCheckInDate(checkInDate));
        }
        if (checkOutDate != null) {
            specification = specification.and(BookingSpecifications.hasCheckOutDate(checkOutDate));
        }
        if (status != null) {
            specification = specification.and(BookingSpecifications.hasStatus(status));
        }

        if (sortBy != null && sortDirection != null) {
            if (sortBy.equalsIgnoreCase("checkInDate") && sortDirection.equalsIgnoreCase("asc")) {
                specification = specification.and(BookingSpecifications.sortByCheckInDateAsc());
            } else if (sortBy.equalsIgnoreCase("checkInDate") && sortDirection.equalsIgnoreCase("desc")) {
                specification = specification.and(BookingSpecifications.sortByCheckInDateDesc());
            } else if (sortBy.equalsIgnoreCase("totalPrice") && sortDirection.equalsIgnoreCase("asc")) {
                specification = specification.and(BookingSpecifications.sortByTotalPriceAsc());
            } else if (sortBy.equalsIgnoreCase("totalPrice") && sortDirection.equalsIgnoreCase("desc")) {
                specification = specification.and(BookingSpecifications.sortByTotalPriceDesc());
            }
        }

        return new ResponseEntity<>(bookingService.searchBookings(specification), HttpStatus.OK);
    }

    @GetMapping("/daily-booking")
    public ResponseEntity<Void> sendDailyBookingReport() {
        bookingService.sendDailyBookingReport();
        return ResponseEntity.ok().build();
    }
}

