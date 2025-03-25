package com.booking_hotel.api.room.service;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.booking.service.BookingService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.reposiroty.RoomRepository;
import com.booking_hotel.api.utils.dateUtils.DateUtils;
import com.booking_hotel.api.utils.dtoUtils.BookingResponseUtils;
import com.booking_hotel.api.utils.dtoUtils.RoomResponseUtils;
import com.booking_hotel.api.utils.hotelUtils.HotelUtils;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final HotelService hotelService;
    private final HotelRepository hotelRepository;

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public Room createRoom(Room room, Long hotelId) {

        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
        if(hotelOptional.isEmpty()) {
            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_HOTEL_MESSAGE);
        }

        Room newRoom = Room.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .hotel(hotelOptional.get())
                .isAvailable(room.getIsAvailable())
                .build();
        roomRepository.save(newRoom);

        List<Room> rooms = roomRepository.findByHotel(hotelOptional.get());
        Double minPrice = rooms.stream()
                .mapToDouble(Room::getPrice)
                .min()
                .orElse(0.0);
        Hotel hotel = hotelOptional.get();
        hotel.setCheapestPrice(minPrice);
        hotelRepository.save(hotel);

        return newRoom;
    }

    @Override
    public List<Room> createRooms(List<Room> rooms, Long hotelId) {
        List<Room> roomList = new ArrayList<>();
        for (Room room : rooms) {
            roomList.add(createRoom(room, hotelId));
        }
        return roomList;
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponse> roomResponseList = new ArrayList<>();
        for(Room room : rooms) {
            RoomResponse roomResponse = RoomResponseUtils.buildRoomResponse(room);
            roomResponseList.add(roomResponse);
        }
        return roomResponseList;
    }

    @Override
    public Room updateRoom(Long id, Room updatedRoom) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setRoomNumber(updatedRoom.getRoomNumber());
            room.setRoomType(updatedRoom.getRoomType());
            room.setPrice(updatedRoom.getPrice());
            room.setIsAvailable(updatedRoom.getIsAvailable());
            return roomRepository.save(room);
        }
        throw new ElementNotFoundException(MessageUtils.NOT_FOUND_ROOM_MESSAGE);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public List<Room> searchRooms(Specification<Room> specification) {
        return roomRepository.findAll(specification);
    }


//    @Override
//    public List<RoomResponse> getRoomsAvailableByHotel(Long hotelId, String checkInDateStr, String checkOutDateStr) {
//
//        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
//        ZonedDateTime checkInDate = ZonedDateTime.parse(checkInDateStr);
//        ZonedDateTime checkOutDate = ZonedDateTime.parse(checkOutDateStr);
//
//        if(hotelOptional.isEmpty()) {
//            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_HOTEL_MESSAGE);
//        }
//
//        List<Room> rooms = roomRepository.findByHotel(hotelOptional.get());
//
//        List<BookingResponse> bookingResponseList = bookingService.getBookingsByHotel(hotelId);
//
//        List<Room> roomsAvailable = new ArrayList<>();
//
//        for(Room room : rooms) {
//            if(HotelUtils.checkRoomAvailability(bookingResponseList, room, checkInDate, checkOutDate)){
//                roomsAvailable.add(room);
//            }
//        }
//
//        return RoomResponseUtils.convertToBookingResponseList(roomsAvailable);
//    }
}
