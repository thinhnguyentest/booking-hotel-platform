package com.booking_hotel.api.booking.repository;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Hotel hotel;
    private Room room;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("Test User");
        // Thiết lập các thuộc tính khác của User nếu cần

        hotel = new Hotel();
        hotel.setName("Test Hotel");
        // Thiết lập các thuộc tính khác của Hotel nếu cần

        room = new Room();
        room.setHotel(hotel);
        // Thiết lập các thuộc tính khác của Room nếu cần

        booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        // Thiết lập các thuộc tính khác của Booking nếu cần

        entityManager.persist(hotel);
        entityManager.persist(user);
        entityManager.persist(room);
        entityManager.persist(booking);
        entityManager.flush();
    }

}
