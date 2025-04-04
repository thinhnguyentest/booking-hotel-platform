package com.booking_hotel.api.booking.service;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.auth.service.mail.EmailService;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.reposiroty.RoomRepository;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.bookingUtils.BookingUtils;
import com.booking_hotel.api.utils.dateUtils.DateUtils;
import com.booking_hotel.api.utils.dtoUtils.BookingResponseUtils;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final EmailService emailService;
    private final HotelRepository hotelRepository;
    private final BookingUtils bookingUtils;

    @Override
    public Optional<BookingResponse> getBookingById(Long id) {
        return bookingRepository.findById(id).map(BookingResponseUtils::buildBookingResponse);
    }

    @Override
    public List<BookingResponse> getBookingByUser(String token) {
        User user = userService.findByUsername(JwtProvider.getUserNameByToken(token))
                .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND));

        List<Booking> bookingList = bookingRepository.findByUser(user);
        return BookingResponseUtils.convertToBookingResponseList(bookingList);
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Long userId) {
        List<Booking> bookingList = bookingRepository.findAll();
        return BookingResponseUtils.convertToBookingResponseList(bookingList.stream()
                .filter(booking -> booking.getUser().getUserId() == userId)
                .toList());
    }

    @Override
    public BookingResponse createBooking(Booking booking, String token, Long roomId) {
        bookingUtils.validateBookingDates(booking);
        Room room = roomService.getRoomById(roomId).orElseThrow(() -> new ElementNotFoundException(NOT_FOUND_ROOM_MESSAGE));

        if(!room.getIsAvailable()){
            throw new DateTimeException(DATE_INVALID_MESSAGE);
        }

        bookingUtils.validateRoomAvailability(room, booking.getCheckInDate(), booking.getCheckOutDate());

        User user = userService.findByUsername(JwtProvider.getUserNameByToken(token))
                .orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND));

        Booking newBooking = buildBooking(booking, user, room);

        try {
            bookingRepository.save(newBooking);
            List<ZonedDateTime> newUnAvailableDates = new ArrayList<>();

            // Generate dates between check-in and check-out
            ZonedDateTime startDate = booking.getCheckInDate();
            ZonedDateTime endDate = booking.getCheckOutDate();

            while (!startDate.isAfter(endDate)) {
                newUnAvailableDates.add(startDate);
                startDate = startDate.plusDays(1);
            }

            // Update room's unavailable dates
            if (room.getUnAvailableDates() == null) {
                room.setUnAvailableDates(new ArrayList<>());
            }
            room.getUnAvailableDates().addAll(newUnAvailableDates);
            roomRepository.save(room);
        } catch (Exception e) {
            throw new PersistenceException("Error saving booking", e);
        }
        return BookingResponseUtils.buildBookingResponse(bookingRepository.save(newBooking));
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingResponseUtils::buildBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Scheduled(cron = "0 0 17 * * ?")
    public void sendDailyBookingReport() {
        hotelRepository.findAll().forEach(hotel -> {
            List<Booking> bookings = bookingRepository.findByUser(hotel.getOwner());
            userService.findByUsername(hotel.getOwner().getUsername()).ifPresent(owner ->
                    emailService.sendDailyBookingReport(owner.getEmail(),
                            "Daily Booking Report - " + DateUtils.now("dd-MM-yyyy HH:mm"),
                            BookingResponseUtils.convertToBookingResponseList(bookings))
            );
        });
    }

    private Booking buildBooking(Booking booking, User user, Room room) {
        return Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status("PENDING")
                .build();
    }

    @Override
    public BookingResponse updateBooking(Long id, Booking updatedBooking) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_BOOKING_MESSAGE));
        booking.setCheckInDate(updatedBooking.getCheckInDate());
        booking.setCheckOutDate(updatedBooking.getCheckOutDate());
        booking.setTotalPrice(updatedBooking.getTotalPrice());
        booking.setStatus(updatedBooking.getStatus());
        return BookingResponseUtils.buildBookingResponse(bookingRepository.save(booking));
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponse> searchBookings(Specification<Booking> specification) {
        return BookingResponseUtils.convertToBookingResponseList(bookingRepository.findAll(specification));
    }

}
