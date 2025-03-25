package com.booking_hotel.api.payment.service;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.mail.EmailService;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.booking.entity.Booking;
import com.booking_hotel.api.booking.repository.BookingRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.payment.dto.PaymentResponse;
import com.booking_hotel.api.payment.dto.StripeResponse;
import com.booking_hotel.api.payment.entity.Payment;
import com.booking_hotel.api.payment.repository.PaymentRepository;
import com.booking_hotel.api.utils.dtoUtils.BookingResponseUtils;
import com.booking_hotel.api.utils.dtoUtils.PaymentResponseUtils;
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Value("${stripe.api.key}")
    private String secretKey;

    @Override
    public PaymentResponse createPayment(Long bookingId, Payment payment) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if(booking.isEmpty()) {
            throw new ElementNotFoundException(MessageUtils.NOT_FOUND_BOOKING_MESSAGE);
        }

        Payment newPayment = Payment.builder()
                                    .amount(BigDecimal.valueOf(booking.get().getTotalPrice()))
                                    .paymentMethod(payment.getPaymentMethod())
                                    .paymentStatus(payment.getPaymentStatus() == null ? PENDING_BOOKING_STATUS : payment.getPaymentStatus())
                                    .booking(booking.get())
                                    .build();

        paymentRepository.save(newPayment);
        return PaymentResponseUtils.buildImageResponse(newPayment);
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException(NOT_FOUND_PAYMENT_MESSAGE));
    }

    @Override
    public StripeResponse checkoutBooking(Long paymentId, String token) {
        Stripe.apiKey = secretKey;
        Optional<User> user = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(user.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if(paymentOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_PAYMENT_MESSAGE);
        }

        Payment newPayment = paymentOptional.get();

        // Create a PaymentIntent with the order amount and currency
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(newPayment.getBooking().getRoom().getHotel().getName())
                        .build();

        // Create new line item with the above product data and associated price
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("USD")
                        .setUnitAmount(newPayment.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                        .setProductData(productData)
                        .build();

        // Create new line item with the above price data
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        // Create new session with the line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/api/payments/checkout/success?paymentId="+paymentId)
                        .setCancelUrl("http://localhost:8080/api/payements/checkout/cancel?paymentId="+paymentId)
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException();
        }

        // send email
        String paymentLink = session.getUrl();
        try {
            emailService.sendEmailPayment(user.get().getEmail(),
                    PAYMENT_SUBJECT_MESSAGE + "\n ", PAYMENT_MESSAGE + paymentLink);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // change status booking after pay
        Booking paymentBooking = newPayment.getBooking();
        paymentBooking.setStatus(COMPLETED_BOOKING_STATUS);
        bookingRepository.save(paymentBooking);

        // change status payment after pay
        newPayment.setPaymentStatus(CONFIRMED_BOOKING_STATUS);
        paymentRepository.save(newPayment);

        return StripeResponse
                .builder()
                .status(PAYMENT_SUCCESS_STATUS)
                .message(PAYMENT_CREATED_MESSAGE)
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    @Override
    public void confirmPayment(Long paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if(paymentOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_PAYMENT_MESSAGE);
        }
        Payment newPayment = paymentOptional.get();
        // change status booking after pay
        Booking paymentBooking = newPayment.getBooking();
        paymentBooking.setStatus(COMPLETED_BOOKING_STATUS);
        String userEmailConfirmedBooking = paymentBooking.getUser().getEmail();
        emailService.sendEmailbookingConfirmed(userEmailConfirmedBooking, SUBJECT_BOOKING_MESSAGE, BookingResponseUtils.buildBookingResponse(paymentBooking));

        bookingRepository.save(paymentBooking);

        // change status payment after pay
        newPayment.setPaymentStatus(CONFIRMED_BOOKING_STATUS);
        paymentRepository.save(newPayment);
    }

    @Override
    public void cancelPayment(Long paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if(paymentOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_PAYMENT_MESSAGE);
        }
        Payment newPayment = paymentOptional.get();
        // change status booking after pay
        Booking paymentBooking = newPayment.getBooking();
        paymentBooking.setStatus(PENDING_BOOKING_STATUS);
        bookingRepository.save(paymentBooking);

        // change status payment after pay
        newPayment.setPaymentStatus(PENDING_BOOKING_STATUS);
        paymentRepository.save(newPayment);
    }
}

