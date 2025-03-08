package com.booking_hotel.api.hotel.service;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    ResponseEntity<HotelResponse> createHotel(Hotel hotel, String username);
    ResponseEntity<List<HotelResponse>> getAllHotels();
    Optional<Hotel> getHotelById(Long id);
    ResponseEntity<?> updateHotel(Long id, Hotel hotelDetail);
    void deleteHotel(Long id);
    List<HotelResponse> convertToHotelResponseList(List<Hotel> hotels);

}
