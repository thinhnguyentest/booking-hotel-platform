package com.booking_hotel.api.utils.hotelUtils;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;

import java.time.ZonedDateTime;
import java.util.List;

public class HotelUtils {
    public static boolean checkRoomAvailability(List<BookingResponse> bookingResponseList,
                                                Room room, ZonedDateTime checkInDate, ZonedDateTime checkOutDate) {
        return bookingResponseList.stream()
                .filter(bookingResponse -> room.getRoomId().equals(bookingResponse.getRoomResponse().getRoomId()))
                .noneMatch(bookingResponse ->
                        checkInDate.isBefore(ZonedDateTime.parse(bookingResponse.getCheckOutDate())) &&
                                checkOutDate.isAfter(ZonedDateTime.parse(bookingResponse.getCheckInDate()))
                );
    }
}
