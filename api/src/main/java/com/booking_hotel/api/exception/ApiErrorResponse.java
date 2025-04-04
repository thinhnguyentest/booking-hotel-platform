package com.booking_hotel.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiErrorResponse {
    private String path;
    private String timestamp;
    private List<String> errors;
}
