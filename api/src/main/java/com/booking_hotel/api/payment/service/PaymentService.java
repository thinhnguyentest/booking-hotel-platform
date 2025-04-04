package com.booking_hotel.api.payment.service;

import com.booking_hotel.api.payment.dto.PaymentResponse;
import com.booking_hotel.api.payment.dto.StripeResponse;
import com.booking_hotel.api.payment.entity.Payment;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    StripeResponse checkoutBooking(Long bookingId, String token);
    ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader);
}
