package com.booking_hotel.api.utils.bookingUtils;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingUtils {

    private final RoomService roomService;
    private final BookingRepository bookingRepository;

    public boolean checkRoomAvailable(Booking bookingReq, Long roomId) {
        Optional<Room> roomOptional = roomService.getRoomById(roomId);
        if(roomOptional.isEmpty()){
            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_ROOM_MESSAGE);
        }

        List<Booking> bookingList = bookingRepository.findByRoom(roomOptional.get());

        if(!roomOptional.get().getIsAvailable()) {
            throw new ElementNotFoundException(MessageUtils.NOT_AVAILABLE_ROOM_MESSAGE);
        }

        for(Booking booking : bookingList) {
            if(bookingReq.getCheckInDate().isBefore(booking.getCheckOutDate()) &&
            bookingReq.getCheckOutDate().isAfter(booking.getCheckInDate())){
                return false;
            }
        }
        return true;
    }
}
