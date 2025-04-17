package com.booking_hotel.api.booking.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.bookingUtils.BookingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private RoomService roomService;

    @Mock
    private BookingUtils bookingUtils;

    private User user;
    private Room room;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1L);
        user.setUsername("Test User");

        room = new Room();
        room.setRoomId(1L);
        room.setIsAvailable(true);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(ZonedDateTime.now());
        booking.setCheckOutDate(ZonedDateTime.now().plusDays(1));
    }

    @Test
    void testDeleteBooking() {
        doNothing().when(bookingRepository).deleteById(1L);

        bookingService.deleteBooking(1L);

        verify(bookingRepository, times(1)).deleteById(1L);
    }
}
