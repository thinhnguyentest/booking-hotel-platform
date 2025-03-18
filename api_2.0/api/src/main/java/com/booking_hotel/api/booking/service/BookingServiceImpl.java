package com.booking_hotel.api.booking.service;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
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
    private final PDFService pdfService;
    private final HotelRepository hotelRepository;

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

        if(!roomOptional.get().getIsAvailable()){
            throw new IllegalArgumentException(NOT_AVAILABLE_ROOM_MESSAGE);
        }
        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(userOptional.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        if(roomOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_ROOM_MESSAGE);
        }
        Booking newBooking = Booking.builder()
                .user(userOptional.get())
                .room(roomOptional.get())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .status("PENDING")
                .build();

        Booking bookingSaved = bookingRepository.save(newBooking);

        Room room = roomOptional.get();
        room.setIsAvailable(false);
        roomRepository.save(room);

        BookingResponse bookingResponse = BookingResponseUtils.buildBookingResponse(newBooking);
        return bookingResponse;
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
    @Scheduled(cron = "0 */5 * * * ?")
    public void sendDailyBookingReport() {
        List<Hotel> hotelList = hotelRepository.findAll();
        for (Hotel hotel : hotelList) {
            List<Booking> bookingList = bookingRepository.findByUser(hotel.getOwner());
            Optional<User> ownerOptional = userService.findByUsername(hotel.getOwner().getUsername());
            if (ownerOptional.isEmpty()) {
                throw new ElementNotFoundException(USER_NOT_FOUND);
            }
            ResponseEntity<byte[]> pdfResponse = pdfService.generateBookingsReport(bookingList);

            // Giả sử bạn có danh sách email của các OWNER
            String ownerEmail = ownerOptional.get().getEmail();
            String subject = "Daily Booking Report";
            String text = "Please find attached the daily booking report.";

            emailService.sendEmailWithAttachment(ownerEmail, subject, text, pdfResponse.getBody(), "DailyBookingReport.pdf");
        }
    }

}
