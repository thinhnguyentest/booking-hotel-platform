package com.booking_hotel.api.room.service;

import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.reposiroty.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setName("Hotel A");

        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setPrice(100.0);
        room.setHotel(hotel);
    }

    @Test
    void getRoomById_Success() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Optional<Room> foundRoom = roomService.getRoomById(1L);

        assertTrue(foundRoom.isPresent());
        assertEquals("101", foundRoom.get().getRoomNumber());
    }

    @Test
    void getRoomById_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> foundRoom = roomService.getRoomById(1L);

        assertFalse(foundRoom.isPresent());
    }

    @Test
    void createRoom_Success() {
        when(hotelService.getHotelById(1L)).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        when(roomRepository.findByHotel(hotel)).thenReturn(List.of(room));

        Room createdRoom = roomService.createRoom(room, 1L);

        assertEquals("101", createdRoom.getRoomNumber());
        verify(hotelRepository).save(hotel);
    }

    @Test
    void createRoom_HotelNotFound() {
        when(hotelService.getHotelById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ElementNotFoundException.class, () -> {
            roomService.createRoom(room, 1L);
        });

        assertEquals("Hotel not found", exception.getMessage());
    }

    @Test
    void updateRoom_NotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ElementNotFoundException.class, () -> {
            roomService.updateRoom(1L, room);
        });

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void deleteRoom_Success() {
        roomService.deleteRoom(1L);
        verify(roomRepository).deleteById(1L);
    }
}
