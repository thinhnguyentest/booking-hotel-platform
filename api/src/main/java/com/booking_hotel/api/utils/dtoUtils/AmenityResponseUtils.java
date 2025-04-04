package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;

import java.util.ArrayList;
import java.util.List;

public class AmenityResponseUtils {
    public static AmenityResponse buildImageResponse(Amenity amenity) {

        return AmenityResponse.builder()
                .amenityId(amenity.getAmenityId())
                .amenityName(amenity.getAmenityName())
                .amenityDescription(amenity.getAmenityDescription())
                .hotelResponse(HotelResponseUtils.buildHotelResponse(amenity.getHotel()))
                .build();
    }

    public static List<AmenityResponse> convertToImageResponseList(List<Amenity> amenities) {
        List<AmenityResponse> AmenityResponseList = new ArrayList<>();
        for (Amenity amenity : amenities) {
            AmenityResponseList.add(buildImageResponse(amenity));
        }
        return AmenityResponseList;
    }
}
