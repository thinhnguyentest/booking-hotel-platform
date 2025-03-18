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

        @PostMapping("/checkout")
        public ResponseEntity<StripeResponse> checkoutProducts(@RequestParam Long paymentId,  @RequestHeader("Authorization") String accessToken) {
            String token = accessToken.substring(7);
            StripeResponse stripeResponse = paymentService.checkoutBooking(paymentId, token);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(stripeResponse);
        }

        @PostMapping("/checkout/success")
        public ResponseEntity<?> confirmCheckout(@RequestParam Long paymentId) {
            paymentService.confirmPayment(paymentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        @PostMapping("/checkout/cancel")
        public ResponseEntity<?> cancelCheckout(@RequestParam Long paymentId) {
            paymentService.cancelPayment(paymentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        @PostMapping()
        public ResponseEntity<PaymentResponse> createPayment(@RequestParam Long bookingId, @RequestBody Payment payment) {

            PaymentResponse paymentResponse = paymentService.createPayment(bookingId, payment);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(paymentResponse);
        }

        @GetMapping()
        public ResponseEntity<PaymentResponse> getPayment(@RequestParam Long paymentId) {

            Payment payment = paymentService.getPayment(paymentId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(PaymentResponseUtils.buildImageResponse(payment));
        }

    }
