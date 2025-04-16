package com.booking_hotel.api.amenity.service;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;
import com.booking_hotel.api.amenity.repository.AmenityRepository;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmenityServiceImplTest {

    @InjectMocks
    private AmenityServiceImpl amenityService;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelService hotelService;

    private Amenity amenity;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotel = new Hotel(); // Khởi tạo một đối tượng Hotel
        amenity = new Amenity();
        amenity.setAmenityName("Swimming Pool");
        amenity.setAmenityDescription("A large swimming pool");
        amenity.setHotel(hotel);
    }

    @Test
    void testCreateAmenity() {
        Long hotelId = 1L;
        when(hotelService.getHotelById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);

        AmenityResponse response = amenityService.createAmenity(amenity, hotelId);

        assertNotNull(response);
        assertEquals("Swimming Pool", response.getAmenityName());
        verify(amenityRepository).save(any(Amenity.class));
    }

    @Test
    void testCreateAmenity_HotelNotFound() {
        Long hotelId = 1L;
        when(hotelService.getHotelById(hotelId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            amenityService.createAmenity(amenity, hotelId);
        });

        assertEquals(MessageUtils.NOT_FOUND_HOTEL_MESSAGE, exception.getMessage());
    }

    @Test
    void testUpdateAmenity() {
        Long amenityId = 1L;
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);

        Amenity updatedAmenity = new Amenity();
        updatedAmenity.setAmenityName("Updated Pool");
        updatedAmenity.setAmenityDescription("An updated description");

        Amenity result = amenityService.updateAmenity(amenityId, updatedAmenity);

        assertEquals("Updated Pool", result.getAmenityName());
        verify(amenityRepository).save(any(Amenity.class));
    }

    @Test
    void testDeleteAmenity() {
        Long amenityId = 1L;
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));

        assertDoesNotThrow(() -> amenityService.deleteAmenity(amenityId));
        verify(amenityRepository).delete(amenity);
    }

    @Test
    void testGetAmenityById() {
        Long amenityId = 1L;
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));

        Optional<AmenityResponse> response = amenityService.getAmenityById(amenityId);

        assertTrue(response.isPresent());
        assertEquals("Swimming Pool", response.get().getAmenityName());
    }

    @Test
    void testGetAllAmenities() {
        List<Amenity> amenities = new ArrayList<>();
        amenities.add(amenity);
        when(amenityRepository.findAll()).thenReturn(amenities);

        List<AmenityResponse> responses = amenityService.getAllAmenities();

        assertEquals(1, responses.size());
        assertEquals("Swimming Pool", responses.get(0).getAmenityName());
    }
}
