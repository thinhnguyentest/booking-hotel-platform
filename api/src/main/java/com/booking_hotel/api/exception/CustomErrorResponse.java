package com.booking_hotel.api.exception;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CustomErrorResponse {
    private String message;
    private String datetime;
    private String path;
    private List<CustomErrorResponse> errors;
}

