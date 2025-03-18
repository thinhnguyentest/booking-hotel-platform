package com.booking_hotel.api.amenity.dto;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AmenityResponse {
    private Long amenityId;

    private String amenityName;

    private String amenityDescription;

    private HotelResponse hotelResponse;
}
