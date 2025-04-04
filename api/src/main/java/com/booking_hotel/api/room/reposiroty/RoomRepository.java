package com.booking_hotel.api.room.reposiroty;

import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    List<Room> findByHotel(Hotel hotel);
}
