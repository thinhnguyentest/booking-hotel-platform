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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;

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
        return Optional.of(BookingResponseUtils.buildBookingResponse(bookingRepository.findById(id).get()));
    }

    @Override
    public BookingResponse createBooking(Booking booking, String token, Long roomId) {
        if(booking.getCheckInDate().isAfter(booking.getCheckOutDate())){
            throw new DateTimeException(DATE_INVALID_MESSAGE);
        }
        Optional<Room> roomOptional = roomService.getRoomById(roomId);
        if(roomOptional.isEmpty()){
            throw new ElementNotFoundException(NOT_FOUND_ROOM_MESSAGE);
        }

        if(!bookingUtils.checkRoomAvailable(booking, roomId)) {
            throw new ElementNotFoundException(NOT_AVAILABLE_ROOM_MESSAGE);
        }

        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(userOptional.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        Booking newBooking = Booking.builder()
                .user(userOptional.get())
                .room(roomOptional.get())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status("PENDING")
                .build();

        bookingRepository.save(newBooking);

        return BookingResponseUtils.buildBookingResponse(newBooking);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return BookingResponseUtils.convertToBookingResponseList(bookingRepository.findAll());
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

    @Override
    @Scheduled(cron = "0 0 17 * * ?") // Chạy mỗi ngày lúc
    public void sendDailyBookingReport() {
        List<Hotel> hotelList = hotelRepository.findAll();
        for (Hotel hotel : hotelList) {
            List<Booking> bookingList = bookingRepository.findByUser(hotel.getOwner());
            Optional<User> ownerOptional = userService.findByUsername(hotel.getOwner().getUsername());
            if (ownerOptional.isEmpty()) {
                throw new ElementNotFoundException(USER_NOT_FOUND);
            }

            String ownerEmail = ownerOptional.get().getEmail(); // Thay bằng email của chủ khách sạn
            String subject = "Daily Booking Report - " + DateUtils.now("dd-MM-yyyy HH:mm");
            emailService.sendDailyBookingReport(ownerEmail, subject, BookingResponseUtils.convertToBookingResponseList(bookingList));
        }
    }

    @Override
    public List<BookingResponse> getBookingsByHotel(Long hotelId) {
        Room room = roomRepository.findById(hotelId).get();
        List<Booking> bookingList = bookingRepository.findByRoom(room);
        return BookingResponseUtils.convertToBookingResponseList(bookingList);
    }
}
