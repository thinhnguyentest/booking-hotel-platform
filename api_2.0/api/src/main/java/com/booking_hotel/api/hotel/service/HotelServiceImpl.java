package com.booking_hotel.api.hotel.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.UserService;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.utils.HotelMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final UserService userService;

    @Override
    public ResponseEntity<HotelResponse> createHotel(Hotel hotel, String username) {
        Optional<User> user = userService.findByUsername(username);
        Hotel newHotel = Hotel.builder()
                .hotelId(hotel.getHotelId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .owner(user.get())
                .build();


        HotelResponse hotelResponse = HotelResponse.builder()
                .name(hotel.getName())
                .country(hotel.getCountry())
                .city(hotel.getCity())
                .description(hotel.getDescription())
                .address(hotel.getAddress())
                .build();

        hotelRepository.save(newHotel);
        System.out.println(hotelResponse);
        return new ResponseEntity<>(hotelResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        return ResponseEntity.ok(convertToHotelResponseList(hotels));
    }

    @Override
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> updateHotel(Long id, Hotel hotelDetail) {
        System.out.println(hotelDetail);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.setName(hotelDetail.getName());
        hotel.setAddress(hotelDetail.getAddress());
        hotel.setCity(hotelDetail.getCity());
        hotel.setCountry(hotelDetail.getCountry());
        hotel.setDescription(hotelDetail.getDescription());

        hotelRepository.save(hotel);

        return ResponseEntity.ok("Updated Hotel");

    }

    @Override
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    @Override
    public List<HotelResponse> convertToHotelResponseList(List<Hotel> hotels) {
        return hotels.stream()
                .map(HotelMapper::toHotelResponse)
                .collect(Collectors.toList());
    }
}
