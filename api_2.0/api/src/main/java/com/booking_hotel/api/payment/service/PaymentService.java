package com.booking_hotel.api.payment.service;

import com.booking_hotel.api.payment.dto.PaymentResponse;
import com.booking_hotel.api.payment.dto.StripeResponse;
import com.booking_hotel.api.payment.entity.Payment;

public interface PaymentService {
    Payment getPayment(Long id);
    StripeResponse checkoutBooking(Long paymentId, String token);
    PaymentResponse createPayment(Long bookingId, Payment payment);
    void cancelPayment (Long paymentId);
    void confirmPayment(Long paymentId);
}
