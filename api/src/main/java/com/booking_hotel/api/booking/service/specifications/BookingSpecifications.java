package com.booking_hotel.api.booking.service.specifications;

import com.booking_hotel.api.booking.entity.Booking;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class BookingSpecifications {
    public static Specification<Booking> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Booking> hasRoomId(Long roomId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("room").get("id"), roomId);
    }

    public static Specification<Booking> hasCheckInDate(ZonedDateTime checkInDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("checkInDate"), checkInDate);
    }

    public static Specification<Booking> hasCheckOutDate(ZonedDateTime checkOutDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("checkOutDate"), checkOutDate);
    }

    public static Specification<Booking> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Booking> sortByCheckInDateAsc() {
        return (root, query, criteriaBuilder) -> query.orderBy(criteriaBuilder.asc(root.get("checkInDate"))).getRestriction();
    }

    public static Specification<Booking> sortByCheckInDateDesc() {
        return (root, query, criteriaBuilder) -> query.orderBy(criteriaBuilder.desc(root.get("checkInDate"))).getRestriction();
    }

    public static Specification<Booking> sortByTotalPriceAsc() {
        return (root, query, criteriaBuilder) -> query.orderBy(criteriaBuilder.asc(root.get("totalPrice"))).getRestriction();
    }

    public static Specification<Booking> sortByTotalPriceDesc() {
        return (root, query, criteriaBuilder) -> query.orderBy(criteriaBuilder.desc(root.get("totalPrice"))).getRestriction();
    }
}

