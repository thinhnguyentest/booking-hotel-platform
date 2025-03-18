package com.booking_hotel.api.booking.dto;

import com.booking_hotel.api.auth.dto.UserResponse;
import com.booking_hotel.api.room.dto.RoomResponse;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private UserResponse userResponse;
    private RoomResponse roomResponse;
    private String checkInDate;
    private String checkOutDate;
    private Double totalPrice;
    private String status;
    private String createdAt;
}
