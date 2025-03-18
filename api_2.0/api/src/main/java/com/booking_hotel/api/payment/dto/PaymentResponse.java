package com.booking_hotel.api.payment.dto;

import com.booking_hotel.api.booking.dto.BookingResponse;
import com.booking_hotel.api.booking.entity.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long paymentId;

    private BookingResponse bookingResponse;

    private BigDecimal amount;

    private String paymentMethod;

    private String paymentStatus;

    private ZonedDateTime paymentDate;
}
