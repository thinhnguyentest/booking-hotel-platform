package com.booking_hotel.api.room.repository;

import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.reposiroty.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Mock
    private Hotel hotel;

    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setName("Hotel A");

        room1 = new Room();
        room1.setRoomId(1L);
        room1.setRoomNumber("101");
        room1.setRoomType("Deluxe");
        room1.setPrice(100.0);
        room1.setIsAvailable(true);
        room1.setHotel(hotel);

        room2 = new Room();
        room2.setRoomId(2L);
        room2.setRoomNumber("102");
        room2.setRoomType("Standard");
        room2.setPrice(80.0);
        room2.setIsAvailable(true);
        room2.setHotel(hotel);

        roomRepository.saveAll(Arrays.asList(room1, room2));
    }

    @Test
    void findByHotel_Success() {
        List<Room> rooms = roomRepository.findByHotel(hotel);

        assertEquals(2, rooms.size());
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("101")));
        assertTrue(rooms.stream().anyMatch(r -> r.getRoomNumber().equals("102")));
    }

    @Test
    void findByHotel_NoRooms() {
        Hotel anotherHotel = new Hotel();
        anotherHotel.setHotelId(2L);
        anotherHotel.setName("Hotel B");

        List<Room> rooms = roomRepository.findByHotel(anotherHotel);

        assertTrue(rooms.isEmpty());
    }
}
