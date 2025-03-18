package com.booking_hotel.api.room.service;

import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoomService {
    Optional<Room> getRoomById(Long id);
    Room createRoom(Room room, Long hotelId);
    List<Room> createRooms(List<Room> rooms, Long hotelId);
    List<RoomResponse> getAllRooms();
    Room updateRoom(Long id, Room updatedRoom);
    void deleteRoom(Long id);
    List<Room> searchRooms(Specification<Room> specification);
}
