package com.booking_hotel.api.hotel.controller;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.dto.CountByCityResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;

import com.booking_hotel.api.hotel.service.specifications.HotelSpecifications;
import com.booking_hotel.api.utils.dtoUtils.HotelResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping("/createHotel")
    public ResponseEntity<HotelResponse> createHotel(@RequestBody Hotel hotel, @CookieValue("access_token") String accessToken) {
        return hotelService.createHotel(hotel, accessToken);
    }

    @PostMapping("/createHotels")
    public ResponseEntity<List<HotelResponse>> createHotels(@RequestBody List<Hotel> hotels, @CookieValue("access_token") String accessToken) {
        return hotelService.createHotels(hotels, accessToken);
    }

    @GetMapping
    public List<HotelResponse> getAllHotels(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        return hotelService.getAllHotels(pageable);
    }

    @GetMapping("/limit")
    public ResponseEntity<List<HotelResponse>> getAllHotelsLimit(@RequestParam int limit) {
        return hotelService.getAllHotelsLimit(limit);
    }

    @GetMapping("/countByCity")
    public ResponseEntity<CountByCityResponse> countByCity() {
        return hotelService.countByCity();
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long hotelId) {
        Optional<Hotel> hotelOptional= hotelService.getHotelById(hotelId);
        if (hotelOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_HOTEL_MESSAGE);
        }
        HotelResponse response = HotelResponseUtils.buildHotelResponse(hotelOptional.get());
        return new ResponseEntity<>(response, HttpStatus.OK);

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

    @GetMapping("/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX][X]") ZonedDateTime checkInDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX][X]") ZonedDateTime checkOutDate
    ) {
        Specification<Hotel> specification = Specification.where(null);
        if (name != null) {
            specification = specification.and(HotelSpecifications.hasName(name));
        }
        if (city != null) {
            specification = specification.and(HotelSpecifications.hasCity(city));
        }
        if (country != null) {
            specification = specification.and(HotelSpecifications.hasCountry(country));
        }
        if (maxPrice != null) {
            specification = specification.and(HotelSpecifications.hasPriceLessThanOrEqual(maxPrice));
        }
        if (minPrice != null) {
            specification = specification.and(HotelSpecifications.hasPriceGreaterThanOrEqual(minPrice));
        }
        // Xử lý logic ngày check-in/out
        if (checkInDate != null && checkOutDate != null) {
            if (checkOutDate.isBefore(checkInDate.plusDays(1))) {
                throw new IllegalArgumentException("checkOutDate phải sau checkInDate ít nhất 1 ngày");
            }
            specification = specification.and(HotelSpecifications.hasAvailableRoomsBetweenDates(checkInDate, checkOutDate));
        } else if (checkInDate != null || checkOutDate != null) {
            throw new IllegalArgumentException("Phải cung cấp cả checkInDate và checkOutDate");
        }
        List<HotelResponse> hotelResponses = hotelService.searchHotelsWithSpecification(specification);
        return ResponseEntity.ok(hotelResponses);
    }

}
