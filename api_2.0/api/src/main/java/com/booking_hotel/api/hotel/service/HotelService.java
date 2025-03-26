package com.booking_hotel.api.hotel.service;

import com.booking_hotel.api.hotel.dto.CountByCityResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface HotelService {
    ResponseEntity<HotelResponse> createHotel(Hotel hotel, String token);
    ResponseEntity<List<HotelResponse>> createHotels(List<Hotel> hotels, String token);
    List<HotelResponse> getAllHotels(Pageable pageable);
    ResponseEntity<List<HotelResponse>> getAllHotelsLimit(int limit);
    Optional<Hotel> getHotelById(Long id);
    ResponseEntity<?> updateHotel(Long id, Hotel hotelDetail);
    void deleteHotel(Long id);
    List<HotelResponse> convertToHotelResponseList(List<Hotel> hotels);
    ResponseEntity<CountByCityResponse> countByCity();
    List<HotelResponse> searchHotelsWithSpecification(Specification<Hotel> specification);
//    List<Hotel> getHotelsHasRoomAvailable(String city, ZonedDateTime checkInDate, ZonedDateTime checkOutDate);
}
