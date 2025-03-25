package com.booking_hotel.api.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountByCityResponse {
    private Map<String, Integer> countByCityMap = new HashMap<>();
}
