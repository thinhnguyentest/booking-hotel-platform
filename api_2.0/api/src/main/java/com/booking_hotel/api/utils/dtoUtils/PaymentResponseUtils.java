package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.payment.dto.PaymentResponse;
import com.booking_hotel.api.payment.dto.StripeResponse;
import com.booking_hotel.api.payment.entity.Payment;

public class PaymentResponseUtils {
    public static PaymentResponse buildImageResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .bookingResponse(BookingResponseUtils.buildBookingResponse(payment.getBooking()))
                .amount(payment.getAmount())
                .build();
    }
}
