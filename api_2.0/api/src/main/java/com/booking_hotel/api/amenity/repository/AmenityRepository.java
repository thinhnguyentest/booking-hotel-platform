package com.booking_hotel.api.amenity.repository;

import com.booking_hotel.api.amenity.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
