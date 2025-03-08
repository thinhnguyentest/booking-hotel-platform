package com.booking_hotel.api.auth.authController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping()
    public String auth() {
        return "test";
    }
}
