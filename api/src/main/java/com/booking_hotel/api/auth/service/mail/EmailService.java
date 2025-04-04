package com.booking_hotel.api.auth.service.mail;

import com.booking_hotel.api.booking.dto.BookingResponse;
import jakarta.mail.MessagingException;

import java.io.File;
import java.util.List;

public interface EmailService {

    void sendEmail(String to, String subject, String text) throws MessagingException;
    void sendEmailbookingConfirmed(String to, String subject, BookingResponse bookingResponse);
    void sendEmailPayment(String to, String subject, String text);
    void sendDailyBookingReport(String to, String subject, List<BookingResponse> bookings);
}
