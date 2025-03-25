package com.booking_hotel.api.auth.service.mail;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.service.PDFService;
import com.booking_hotel.api.utils.dateUtils.DateUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendEmailbookingConfirmed(String to, String subject, BookingResponse bookingResponse) {
        String htmlContent = formatBookingResponse(bookingResponse);
        sendHtmlEmail(to, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates that the text is HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Async
    @Override
    public void sendEmailPayment(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String formatBookingResponse(BookingResponse bookingResponse) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>");
        sb.append("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css'>");
        sb.append("<style>");
        sb.append("body { font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0; }");
        sb.append("h1 { color: #333; margin-top: 20px; }");
        sb.append("p { font-size: 16px; margin-bottom: 10px; }");
        sb.append("ul { list-style-type: none; padding: 0; }");
        sb.append("li { background: #f4f4f4; margin: 5px 0; padding: 10px; border-radius: 5px; }");
        sb.append("li strong { display: inline-block; width: 150px; }");
        sb.append(".footer { margin-top: 20px; font-size: 14px; color: #777; }");
        sb.append(".booking-info { background-color: #0072C6; color: #fff; padding: 20px; text-align: center; }");
        sb.append(".booking-info h1 { color: #fff; }");
        sb.append("</style>");
        sb.append("</head><body>");

        sb.append("<div class='booking-info'>");
        sb.append("<h1><i class='fas fa-check-circle'></i> Dear ").append(bookingResponse.getUserResponse().getUsername()).append(",</h1>");
        sb.append("<p>Your booking has been confirmed. Here are the details:</p>");
        sb.append("</div>");

        sb.append("<ul>");
        sb.append("<li><strong>Booking ID:</strong> ").append(bookingResponse.getBookingId()).append("</li>");
        sb.append("<li><strong>Hotel :</strong> ").append(bookingResponse.getRoomResponse().getHotelResponse().getName()).append("</li>");
        sb.append("<li><strong>Address :</strong> ").append(bookingResponse.getRoomResponse().getHotelResponse().getAddress()).append("</li>");
        sb.append("<li><strong>Room:</strong> ").append(bookingResponse.getRoomResponse().getRoomNumber()).append("</li>");
        sb.append("<li><strong>Check-In Date:</strong> ").append(DateUtils.formatZonedDateTimeString(bookingResponse.getCheckInDate())).append("</li>");
        sb.append("<li><strong>Check-Out Date:</strong> ").append(DateUtils.formatZonedDateTimeString(bookingResponse.getCheckOutDate())).append("</li>");
        sb.append("<li><strong>Total Price:</strong> ").append(String.format("%.2f", bookingResponse.getTotalPrice())).append("$</li>");
        sb.append("</ul>");

        sb.append("<p>We would like to express our sincere gratitude for choosing ").append(bookingResponse.getRoomResponse().getHotelResponse().getName()).append(" for your stay. We are excited to welcome you and hope that you will have a wonderful experience with us.\n" +
                "\n" +
                "If you have any special requests or need assistance during your stay, please do not hesitate to contact us. Our staff is always ready to help you.\n" +
                "\n" +
                "Once again, thank you for your trust and for choosing us. We wish you an enjoyable and memorable trip!.</p>");
        sb.append("<p class='footer'>Best regards,<br>Hotel Management</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    public void sendEmailWithAttachment(String to, String subject, String text, File file) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            helper.addAttachment("Daily_Booking_Report.pdf", new FileSystemResource(file));

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendDailyBookingReport(String to, String subject, List<BookingResponse> bookings) {
        try {
            File pdfReport = PDFService.generateDailyBookingReport(bookings);
            sendEmailWithAttachment(to, subject, "Please find the daily booking report attached.", pdfReport);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
