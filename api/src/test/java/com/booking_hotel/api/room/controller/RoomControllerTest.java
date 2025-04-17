package com.booking_hotel.api.room.controller;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.dtoUtils.RoomResponseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomControllerTest {

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService roomService;

    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber("101");
        room.setRoomType("Deluxe");
        room.setPrice(100.0);
        room.setIsAvailable(true);
    }

    @Test
    void getRoomById_NotFound() {
        when(roomService.getRoomById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ElementNotFoundException.class, () ->
                roomController.getRoomById(1L));

        assertEquals("Room not found", exception.getMessage());
    }

    @Test
    void deleteRoom_Success() {
        doNothing().when(roomService).deleteRoom(anyLong());

        ResponseEntity<?> response = roomController.deleteRoom(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room deleted successfully", response.getBody());
    }

}
