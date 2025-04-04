package com.booking_hotel.api.amenity.controller;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;
import com.booking_hotel.api.amenity.service.AmenityService;
import com.booking_hotel.api.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    private final HotelService hotelService;

    @GetMapping
    public List<AmenityResponse> getAllAmenities() {
        return amenityService.getAllAmenities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmenityResponse> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AmenityResponse createAmenity(@RequestBody Amenity amenity, @RequestParam Long hotelId) {
        return amenityService.createAmenity(amenity, hotelId);
    }

    @PostMapping("/createAmenities")
    public List<AmenityResponse> createAmenities(@RequestBody List<Amenity> amenities, @RequestParam Long hotelId) {
        return amenityService.createAmenites(amenities, hotelId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long id, @RequestBody Amenity amenityDetails) {
        Amenity updatedAmenity = amenityService.updateAmenity(id, amenityDetails);
        return ResponseEntity.ok(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}

