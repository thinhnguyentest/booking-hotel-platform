package com.booking_hotel.api.image.repository;

import com.booking_hotel.api.image.entity.Image;
import com.booking_hotel.api.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findImagesByRoom(Room room);
}
