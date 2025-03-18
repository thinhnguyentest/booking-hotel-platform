package com.booking_hotel.api.booking.repository;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    List<Booking> findByUser(User user);
}
