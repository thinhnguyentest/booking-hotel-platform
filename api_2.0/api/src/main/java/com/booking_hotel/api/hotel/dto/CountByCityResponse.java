package com.booking_hotel.api.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Builder
@Data
public class CountByCityResponse {
    private Map<String, Integer> countByCityMap = new HashMap<>();
}
