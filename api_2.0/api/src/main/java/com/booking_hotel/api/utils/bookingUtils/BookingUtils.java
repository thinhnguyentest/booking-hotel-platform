package com.booking_hotel.api.utils.bookingUtils;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils.DATE_INVALID_MESSAGE;

@Service
@RequiredArgsConstructor
public class BookingUtils {

    public void validateBookingDates(Booking booking) {
        if (booking.getCheckInDate().isAfter(booking.getCheckOutDate())) {
            throw new DateTimeException(DATE_INVALID_MESSAGE);
        }
    }

    public void validateRoomAvailability(Room room, ZonedDateTime checkInDate, ZonedDateTime checkOutDate) {
        List<ZonedDateTime> unavailableDates = room.getUnAvailableDates();
        boolean isConflict = unavailableDates.stream()
                .anyMatch(date -> date.equals(checkInDate) ||
                        (date.isAfter(checkInDate) && date.isBefore(checkOutDate)));

        if (isConflict) throw new DateTimeException(DATE_INVALID_MESSAGE);
    }
}
