package com.booking_hotel.api.amenity.service;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;

import java.util.List;
import java.util.Optional;

public interface AmenityService {
    void deleteAmenity(Long id);
    Amenity updateAmenity(Long id, Amenity amenityDetails);
    AmenityResponse createAmenity(Amenity amenity, Long hotelId);
    List<AmenityResponse> createAmenites(List<Amenity> amenity, Long hotelId);
    Optional<AmenityResponse> getAmenityById(Long id);
    List<AmenityResponse> getAllAmenities();
}
