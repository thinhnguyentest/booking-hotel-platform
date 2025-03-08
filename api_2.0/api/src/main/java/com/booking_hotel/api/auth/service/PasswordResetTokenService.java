package com.booking_hotel.api.auth.service;

import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
public interface PasswordResetTokenService {
    PasswordResetToken createToken(User user);
    boolean isValidToken(String token);
    Optional<User> getUserByToken(String token);
    PasswordResetToken findByUser(User user);
    void deleteToken(String token);
}
