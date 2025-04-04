package com.booking_hotel.api.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
    private Double rating;
    private String photos;
    private Double cheapestPrice;
}
