package com.booking_hotel.api.hotel.service.specifications;

import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class HotelSpecifications {
    public static Specification<Hotel> hasName(String name) {
        System.out.println("name" + name);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("city"), "%" + city + "%");
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("country"), "%" + country + "%");
    }

    public static Specification<Hotel> hasPriceGreaterThanOrEqual(Double minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("cheapestPrice"), minPrice);
    }

    public static Specification<Hotel> hasPriceLessThanOrEqual(Double maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("cheapestPrice"), maxPrice);
    }

    public static Specification<Hotel> hasAvailableRoomsBetweenDates(
            ZonedDateTime  checkInDate,
            ZonedDateTime checkOutDate
    ) {
        return (root, query, cb) -> {
            // Join Hotel với Room qua quan hệ OneToMany (cần khai báo trong Hotel entity)
            Join<Hotel, Room> roomJoin = root.join("rooms", JoinType.INNER);

            // Subquery kiểm tra unavailable dates không trùng với khoảng ngày
            Subquery<Long> dateConflictSubquery = query.subquery(Long.class);
            Root<Room> conflictingRoom = dateConflictSubquery.from(Room.class);
            Join<Room, ZonedDateTime> dateJoin = conflictingRoom.join("unAvailableDates");

            dateConflictSubquery.select(conflictingRoom.get("roomId"))
                    .where(cb.and(
                            cb.equal(conflictingRoom, roomJoin), // Liên kết với room đang xét
                            cb.between(dateJoin, checkInDate, checkOutDate) // Ngày unavailable trùng khoảng
                    ));

            // Điều kiện tổng hợp
            return cb.and(
                    cb.isTrue(roomJoin.get("isAvailable")), // Phòng phải available
                    cb.not(cb.exists(dateConflictSubquery)) // Không có ngày unavailable trùng
            );
        };
    }

}

