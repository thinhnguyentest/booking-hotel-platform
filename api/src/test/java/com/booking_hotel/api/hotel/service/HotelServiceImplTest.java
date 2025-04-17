package com.booking_hotel.api.hotel.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.hotel.dto.CountByCityResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private User user;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testUser");
        Role role = Role.builder()
                .roleName("ROLE_USER")
                .build();
        user.setRoles(new HashSet<>(Collections.singletonList(role)));

        hotel = Hotel.builder()
                .hotelId(1L)
                .name("Test Hotel")
                .address("123 Test St")
                .city("Test City")
                .country("Test Country")
                .description("Test Description")
                .cheapestPrice(100.0)
                .owner(user)
                .build();
    }

    @Test
    void testCountByCity() {
        when(hotelRepository.findAll()).thenReturn(Collections.singletonList(hotel));

        ResponseEntity<CountByCityResponse> response = hotelService.countByCity();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getCountByCityMap().get("Test City").intValue());
    }

    @Test
    void testUpdateHotel_Success() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        ResponseEntity<?> response = hotelService.updateHotel(1L, hotel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Hotel", response.getBody());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testDeleteHotel() {
        doNothing().when(hotelRepository).deleteById(anyLong());

        hotelService.deleteHotel(1L);

        verify(hotelRepository, times(1)).deleteById(1L);
    }
}
