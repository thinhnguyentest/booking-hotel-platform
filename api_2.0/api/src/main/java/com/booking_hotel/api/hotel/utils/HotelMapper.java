package com.booking_hotel.api.hotel.utils;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;

public class HotelMapper {

    public static HotelResponse toHotelResponse(Hotel hotel) {
        return HotelResponse.builder()
                .name(hotel.getName())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .description(hotel.getDescription())
                .build();
    }
}

