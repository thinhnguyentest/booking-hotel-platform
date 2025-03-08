package com.booking_hotel.api.auth.service;

import com.booking_hotel.api.auth.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
}
