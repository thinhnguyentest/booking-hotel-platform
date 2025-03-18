package com.booking_hotel.api.room.controller;

import com.booking_hotel.api.utils.dtoUtils.RoomResponseUtils;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.room.service.specifications.RoomSpecifications;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        Optional<Room> roomOptional = roomService.getRoomById(id);
        if (roomOptional.isEmpty()) {
            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_ROOM_MESSAGE);
        }
        return new ResponseEntity<>(RoomResponseUtils.buildRoomResponse(roomOptional.get()), HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody Room room, @RequestParam Long hotelId) {
        Room createdRoom = roomService.createRoom(room, hotelId);
        return new ResponseEntity<>(RoomResponseUtils.buildRoomResponse(createdRoom), HttpStatus.CREATED);
    }

    @PostMapping("/addRooms")
    public ResponseEntity<List<RoomResponse>> createRooms(@RequestBody List<Room> rooms, @RequestParam Long hotelId) {
        List<Room> createdRooms = roomService.createRooms(rooms, hotelId);
        List<RoomResponse> roomResponseList = createdRooms.stream().map(RoomResponseUtils::buildRoomResponse).collect(Collectors.toList());
        return new ResponseEntity<>(roomResponseList, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> roomResponseList = roomService.getAllRooms();
        return new ResponseEntity<>(roomResponseList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        Room room = roomService.updateRoom(id, updatedRoom);
        return new ResponseEntity<>(RoomResponseUtils.buildRoomResponse(room), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<RoomResponse>> searchRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {
        Specification<Room> specification = Specification.where(null);
        if (roomNumber != null) {
            specification = specification.and(RoomSpecifications.hasRoomNumber(roomNumber));
        }
        if (roomType != null) {
            specification = specification.and(RoomSpecifications.hasRoomType(roomType));
        }
        if (hotelId != null) {
            specification = specification.and(RoomSpecifications.hasHotelId(hotelId));
        }
        if (minPrice != null) {
            specification = specification.and(RoomSpecifications.hasPriceGreaterThanOrEqual(minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and(RoomSpecifications.hasPriceLessThanOrEqual(maxPrice));
        }
        if (isAvailable != null) {
            if (isAvailable) {
                specification = specification.and(RoomSpecifications.isAvailable());
            } else {
                specification = specification.and(Specification.not(RoomSpecifications.isAvailable()));
            }
        }

        if (sortBy != null && sortDirection != null) {
            if (sortBy.equalsIgnoreCase("roomNumber") && sortDirection.equalsIgnoreCase("asc")) {
                specification = specification.and(RoomSpecifications.sortByRoomNumberAsc());
            } else if (sortBy.equalsIgnoreCase("roomNumber") && sortDirection.equalsIgnoreCase("desc")) {
                specification = specification.and(RoomSpecifications.sortByRoomNumberDesc());
            } else if (sortBy.equalsIgnoreCase("price") && sortDirection.equalsIgnoreCase("asc")) {
                specification = specification.and(RoomSpecifications.sortByPriceAsc());
            } else if (sortBy.equalsIgnoreCase("price") && sortDirection.equalsIgnoreCase("desc")) {
                specification = specification.and(RoomSpecifications.sortByPriceDesc());
            }
        }

        List<Room> roomList = roomService.searchRooms(specification);
        List<RoomResponse> roomResponsesList = roomList.stream().map(RoomResponseUtils::buildRoomResponse).collect(Collectors.toList());

        return new ResponseEntity<>(roomResponsesList, HttpStatus.OK);
    }
}

