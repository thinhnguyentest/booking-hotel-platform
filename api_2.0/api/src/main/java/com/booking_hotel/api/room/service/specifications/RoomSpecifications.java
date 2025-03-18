package com.booking_hotel.api.room.service.specifications;

import com.booking_hotel.api.room.entity.Room;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecifications {
    public static Specification<Room> hasRoomNumber(String roomNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("roomNumber"), "%" + roomNumber + "%");
    }

    public static Specification<Room> hasRoomType(String roomType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("roomType"), "%" + roomType + "%");
    }

    public static Specification<Room> hasHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("hotel").get("id"), hotelId);
    }

    public static Specification<Room> hasPriceGreaterThanOrEqual(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Room> hasPriceLessThanOrEqual(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Room> isAvailable() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isAvailable"));
    }

    public static Specification<Room> sortByRoomNumberAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("roomNumber")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Room> sortByRoomNumberDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("roomNumber")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Room> sortByPriceAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("price")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Room> sortByPriceDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("price")));
            return criteriaBuilder.conjunction();
        };
    }
}

