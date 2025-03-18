package com.booking_hotel.api.hotel.service.specifications;

import com.booking_hotel.api.hotel.entity.Hotel;
import org.springframework.data.jpa.domain.Specification;

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

}

