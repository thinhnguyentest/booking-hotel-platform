package com.booking_hotel.api.payment.service.specifications;

import com.booking_hotel.api.payment.entity.Payment;
import org.springframework.data.jpa.domain.Specification;

public class PaymentSpecification {

    public static Specification<Payment> hasPaymentStatus(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paymentStatus"), status);
    }

}
