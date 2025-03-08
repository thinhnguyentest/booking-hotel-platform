package com.booking_hotel.api.auth.config;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private boolean status;
    private String message;
    private String session;
    private String username;
}
