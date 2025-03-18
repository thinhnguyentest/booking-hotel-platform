package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.auth.dto.UserResponse;
import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.room.dto.RoomResponse;
import static com.booking_hotel.api.utils.dateUtils.DateUtils. *;

import java.util.ArrayList;
import java.util.List;

public class BookingResponseUtils {
    public static BookingResponse buildBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .userResponse(UserResponseUtils.buildUserResponse(booking.getUser()))
                .roomResponse(RoomResponseUtils.buildRoomResponse(booking.getRoom()))
                .checkInDate(formatZonedDateTime(booking.getCheckInDate(), DATE_TIME_FORMAT))
                .checkOutDate(formatZonedDateTime(booking.getCheckOutDate(), DATE_TIME_FORMAT))
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(formatZonedDateTime(booking.getCreatedAt(), DATE_TIME_FORMAT))
                .build();
    }

    public static List<BookingResponse> convertToBookingResponseList(List<Booking> bookings) {
        List<BookingResponse> bookingResponseList = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponseList.add(buildBookingResponse(booking));
        }
        return bookingResponseList;
    }
}
