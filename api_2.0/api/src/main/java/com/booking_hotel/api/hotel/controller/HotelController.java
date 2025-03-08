package com.booking_hotel.api.hotel.controller;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")

public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/{username}")
    public ResponseEntity<HotelResponse> createHotel(@RequestBody Hotel hotel, @PathVariable String username) {
        return hotelService.createHotel(hotel, username);
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        if(hotel.isEmpty()) {
            throw new ElementNotFoundException("Hotel not found");
        }
        HotelResponse response = HotelResponse.builder()
                .name(hotel.get().getName())
                .city(hotel.get().getCity())
                .address(hotel.get().getAddress())
                .description(hotel.get().getDescription())
                .country(hotel.get().getCountry()).build();
        return new ResponseEntity<>(response, HttpStatus.FOUND);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetail) {
        System.out.println(hotelDetail);
        return hotelService.updateHotel(id, hotelDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok("Hotel deleted successfully");
    }
}
