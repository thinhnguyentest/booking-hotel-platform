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
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Override
    public StripeResponse checkoutBooking(Long bookingId, String token) {
        Stripe.apiKey = secretKey;
        Optional<User> user = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(user.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_BOOKING_MESSAGE);
        }

        Booking newBooking = bookingOptional.get();

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(newBooking.getRoom().getHotel().getName())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("USD")
                        .setUnitAmount((long)(newBooking.getTotalPrice() * 100))
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .putMetadata("bookingId", bookingId.toString())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/checkout/success?bookingId=" + bookingId)
                        .setCancelUrl("http://localhost:3000/checkout/cancel?bookingId=" + bookingId)
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException();
        }

        return StripeResponse
                .builder()
                .status(PAYMENT_SUCCESS_STATUS)
                .message(PAYMENT_CREATED_MESSAGE)
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    @Override
    public ResponseEntity<String> handleStripeWebhook(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );

            switch (event.getType()) {
                case "checkout.session.completed":
                    confirmPayment(event);
                    break;
                case "checkout.session.expired":
                case "checkout.session.async_payment_failed":
                    cancelPayment(event);
                    break;
                default:
                    return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok("Webhook processed");
        } catch (StripeException | JsonSyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid payload");
        }
    }

    public void confirmPayment(Event event) throws StripeException {
        Session session = (Session) event.getData().getObject();
        Long bookingId = Long.parseLong(session.getMetadata().get("bookingId"));
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_BOOKING_MESSAGE);
        }
        // change status booking after pay
        Booking paymentBooking = bookingOptional.get();
        paymentBooking.setStatus(COMPLETED_BOOKING_STATUS);
        String userEmailConfirmedBooking = paymentBooking.getUser().getEmail();
        emailService.sendEmailbookingConfirmed(userEmailConfirmedBooking, SUBJECT_BOOKING_MESSAGE, BookingResponseUtils.buildBookingResponse(paymentBooking));
        bookingRepository.save(paymentBooking);

        paymentBooking.setStatus(COMPLETED_BOOKING_STATUS);
        bookingRepository.save(paymentBooking);
    }

    public void cancelPayment(Event event) {
        Session session = (Session) event.getData().getObject();
        Long bookingId = Long.parseLong(session.getMetadata().get("bookingId"));
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_PAYMENT_MESSAGE);
        }
        // change status booking after pay
        Booking paymentBooking = bookingOptional.get();
        paymentBooking.setStatus(PENDING_BOOKING_STATUS);
        bookingRepository.save(paymentBooking);

    }
}

