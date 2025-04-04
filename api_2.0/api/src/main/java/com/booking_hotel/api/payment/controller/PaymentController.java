    package com.booking_hotel.api.payment.controller;

    import com.booking_hotel.api.payment.dto.PaymentResponse;
    import com.booking_hotel.api.payment.dto.StripeResponse;
    import com.booking_hotel.api.payment.entity.Payment;
    import com.booking_hotel.api.payment.service.PaymentService;
    import com.booking_hotel.api.utils.dtoUtils.PaymentResponseUtils;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/payments")
    public class PaymentController {
        private final PaymentService paymentService;

        @PostMapping("/webhook")
        public ResponseEntity<String> handleWebhook(
                @RequestBody String payload,
                @RequestHeader("Stripe-Signature") String sigHeader) {

            return paymentService.handleStripeWebhook(payload, sigHeader);
        }

        @PostMapping("/checkout")
        public ResponseEntity<StripeResponse> checkoutProducts(@RequestParam Long bookingId,  @CookieValue("access_token") String accessToken) {
            StripeResponse stripeResponse = paymentService.checkoutBooking(bookingId, accessToken);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(stripeResponse);
        }

    }
