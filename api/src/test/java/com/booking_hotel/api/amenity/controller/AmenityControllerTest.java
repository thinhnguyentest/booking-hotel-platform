package com.booking_hotel.api.amenity.controller;

import com.booking_hotel.api.amenity.dto.AmenityResponse;
import com.booking_hotel.api.amenity.entity.Amenity;
import com.booking_hotel.api.amenity.service.AmenityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WebMvcTest(AmenityController.class)
class AmenityControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private AmenityController amenityController;

    @Autowired
    private AmenityService amenityService;

    @BeforeEach
    void setUp() {
        amenityService = mock(AmenityService.class);
        amenityController = new AmenityController(amenityService);
        mockMvc = MockMvcBuilders.standaloneSetup(amenityController).build();
    }

    @Test
    void testGetAllAmenities() throws Exception {
        when(amenityService.getAllAmenities()).thenReturn(Collections.emptyList());

        List<AmenityResponse> amenities = amenityController.getAllAmenities();

        assertThat(amenities).isNotNull();
        assertThat(amenities).isEmpty();
        verify(amenityService, times(1)).getAllAmenities();
    }

    @Test
    void testGetAmenityById_Found() {
        Long id = 1L;
        AmenityResponse amenityResponse = AmenityResponse.builder().build();
        when(amenityService.getAmenityById(id)).thenReturn(Optional.of(amenityResponse));

        ResponseEntity<AmenityResponse> response = amenityController.getAmenityById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(amenityResponse);
    }

    @Test
    void testGetAmenityById_NotFound() {
        Long id = 1L;
        when(amenityService.getAmenityById(id)).thenReturn(Optional.empty());

        ResponseEntity<AmenityResponse> response = amenityController.getAmenityById(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testCreateAmenity() {
        Amenity amenity = new Amenity();
        Long hotelId = 1L;
        AmenityResponse amenityResponse = AmenityResponse.builder().build();
        when(amenityService.createAmenity(any(Amenity.class), anyLong())).thenReturn(amenityResponse);

        AmenityResponse response = amenityController.createAmenity(amenity, hotelId);

        assertThat(response).isEqualTo(amenityResponse);
    }

    @Test
    void testUpdateAmenity() {
        Long id = 1L;
        Amenity amenityDetails = new Amenity();
        Amenity updatedAmenity = new Amenity();
        when(amenityService.updateAmenity(anyLong(), any(Amenity.class))).thenReturn(updatedAmenity);

        ResponseEntity<Amenity> response = amenityController.updateAmenity(id, amenityDetails);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(updatedAmenity);
    }

    @Test
    void testDeleteAmenity() {
        Long id = 1L;
        doNothing().when(amenityService).deleteAmenity(id);

        ResponseEntity<Void> response = amenityController.deleteAmenity(id);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(amenityService, times(1)).deleteAmenity(id);
    }
}
