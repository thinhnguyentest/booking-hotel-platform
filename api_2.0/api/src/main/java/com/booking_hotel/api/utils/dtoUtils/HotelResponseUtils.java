package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;

public class HotelResponseUtils {
    public static HotelResponse buildHotelResponse(Hotel hotel) {
        HotelResponse response = HotelResponse.builder()
                .id(hotel.getHotelId())
                .name(hotel.getName())
                .city(hotel.getCity())
                .address(hotel.getAddress())
                .description(hotel.getDescription())
                .country(hotel.getCountry())
                .cheapestPrice(hotel.getCheapestPrice())
                .photos(hotel.getPhotos())
                .rating(hotel.getRating())
                .build();
        return response;
    }
}
