package com.booking_hotel.api.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class HotelResponse {
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
}
