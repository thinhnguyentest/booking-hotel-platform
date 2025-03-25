package com.booking_hotel.api.hotel.controller;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.dto.CountByCityResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.hotel.service.specifications.HotelSpecifications;
import com.booking_hotel.api.utils.dtoUtils.HotelResponseUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelService hotelService;

    @Test
    void testCreateHotel() {
        Hotel hotel = new Hotel();
        String accessToken = "Bearer abc123";
        when(hotelService.createHotel(hotel, "abc123")).thenReturn(new ResponseEntity<>(new HotelResponse(), HttpStatus.CREATED));

        ResponseEntity<HotelResponse> response = hotelController.createHotel(hotel, accessToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(hotelService, times(1)).createHotel(hotel, "abc123");
    }

    @Test
    void testCreateHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String accessToken = "Bearer abc123";
        when(hotelService.createHotels(hotels, "abc123")).thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.CREATED));

        ResponseEntity<List<HotelResponse>> response = hotelController.createHotels(hotels, accessToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(hotelService, times(1)).createHotels(hotels, "abc123");
    }

    @Test
    void testGetAllHotels() {
        Pageable pageable = PageRequest.of(0, 10);
        when(hotelService.getAllHotels(pageable)).thenReturn(new ArrayList<>());

        List<HotelResponse> response = hotelController.getAllHotels(0, 10);

        assertNotNull(response);
        verify(hotelService, times(1)).getAllHotels(pageable);
    }

    @Test
    void testGetAllHotelsLimit() {
        int limit = 5;
        when(hotelService.getAllHotelsLimit(limit)).thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));

        ResponseEntity<List<HotelResponse>> response = hotelController.getAllHotelsLimit(limit);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelService, times(1)).getAllHotelsLimit(limit);
    }

    @Test
    void testCountByCity() {
        when(hotelService.countByCity()).thenReturn(new ResponseEntity<>(new CountByCityResponse(), HttpStatus.OK));

        ResponseEntity<CountByCityResponse> response = hotelController.countByCity();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelService, times(1)).countByCity();
    }

    @Test
    void testGetHotelByIdNotFound() {
        Long id = 1L;
        when(hotelService.getHotelById(id)).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> hotelController.getHotelById(id));
        verify(hotelService, times(1)).getHotelById(id);
    }

    @Test
    void testUpdateHotel() {
        Long id = 1L;
        Hotel hotelDetail = new Hotel();
        when(hotelService.updateHotel(id, hotelDetail)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = hotelController.updateHotel(id, hotelDetail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelService, times(1)).updateHotel(id, hotelDetail);
    }

    @Test
    void testDeleteHotel() {
        Long id = 1L;
        doNothing().when(hotelService).deleteHotel(id);

        ResponseEntity<?> response = hotelController.deleteHotel(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelService, times(1)).deleteHotel(id);
    }

    @Test
    void testSearchHotels() {
        String name = "Hotel A";
        String city = "New York";
        String country = "USA";
        Double maxPrice = 200.0;
        Double minPrice = 50.0;
        Specification<Hotel> specification = Specification.where(null);
        specification = specification.and(HotelSpecifications.hasName(name));
        specification = specification.and(HotelSpecifications.hasCity(city));
        specification = specification.and(HotelSpecifications.hasCountry(country));
        specification = specification.and(HotelSpecifications.hasPriceLessThanOrEqual(maxPrice));
        specification = specification.and(HotelSpecifications.hasPriceGreaterThanOrEqual(minPrice));
        when(hotelService.searchHotelsWithSpecification(specification)).thenReturn(new ArrayList<>());

        ResponseEntity<List<HotelResponse>> response = hotelController.searchHotels(name, city, country, maxPrice, minPrice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelService, times(1)).searchHotelsWithSpecification(specification);
    }
}
