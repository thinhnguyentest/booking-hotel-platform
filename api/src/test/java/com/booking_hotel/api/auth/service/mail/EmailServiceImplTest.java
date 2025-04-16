package com.booking_hotel.api.auth.service.mail;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.auth.dto.UserResponse;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = EmailServiceImplTest.TestConfig.class)
class EmailServiceImplTest {

    private EmailServiceImpl emailService;
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        emailService = new EmailServiceImpl(javaMailSender);
    }

    @Test
    void testSendEmailBookingConfirmed_HTML() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        BookingResponse booking = BookingResponse.builder()
                .bookingId(1L)
                .userResponse(UserResponse.builder().username("John Doe").build())
                .roomResponse(RoomResponse.builder()
                        .roomNumber("101")
                        .hotelResponse(HotelResponse.builder().name("Hotel Test").address("123 Main St").build())
                        .build())
                .checkInDate(ZonedDateTime.now().toString())
                .checkOutDate(ZonedDateTime.now().toString())
                .totalPrice(100.0)
                .build();

        emailService.sendEmailbookingConfirmed("thinhnguyenndt248@gmail.com", "Test Subject", booking);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Configuration
    static class TestConfig {
        @Bean
        public JavaMailSender javaMailSender() {
            return Mockito.mock(JavaMailSender.class);
        }
    }
}
