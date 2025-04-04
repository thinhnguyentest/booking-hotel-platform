package com.booking_hotel.api.room.dto;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long roomId;

    private HotelResponse hotelResponse;

    private String roomNumber;

    private String roomType;

    private Double price;

    private Boolean isAvailable;

    private List<ZonedDateTime> unAvailableDates;
}
