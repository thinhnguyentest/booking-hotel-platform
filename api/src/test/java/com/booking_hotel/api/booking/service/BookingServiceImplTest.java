package com.booking_hotel.api.booking.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.bookingUtils.BookingUtils;
import com.booking_hotel.api.utils.dtoUtils.BookingResponseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testGetBookingById() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<BookingResponse> response = bookingService.getBookingById(1L);

        assertTrue(response.isPresent());
        assertEquals(booking.getBookingId(), response.get().getBookingId());
    }

    @Test
    void testCreateBooking() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));

        // Không cần mock validateBookingDates, chỉ cần gọi trực tiếp
        bookingUtils.validateBookingDates(booking); // Gọi trực tiếp

        when(bookingRepository.save(any())).thenReturn(booking);

        BookingResponse response = bookingService.createBooking(booking, "dummyToken", 1L);

        assertNotNull(response);
        assertEquals(booking.getBookingId(), response.getBookingId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }



    @Test
    void testGetBookingsByUserId() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        var bookings = bookingService.getBookingsByUserId(user.getUserId());

        assertEquals(1, bookings.size());
        assertEquals(booking.getBookingId(), bookings.get(0).getBookingId());
    }

    @Test
    void testDeleteBooking() {
        doNothing().when(bookingRepository).deleteById(1L);

        bookingService.deleteBooking(1L);

        verify(bookingRepository, times(1)).deleteById(1L);
    }
}
