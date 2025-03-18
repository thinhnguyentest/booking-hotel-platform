package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;

public class RoomResponseUtils {
    public static RoomResponse buildRoomResponse(Room room) {
        Hotel hotel = room.getHotel();
        HotelResponse hotelResponse = HotelResponseUtils.buildHotelResponse(hotel);

        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .isAvailable(room.getIsAvailable())
                .hotelResponse(hotelResponse)
                .build();
    }
}
