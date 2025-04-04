package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;

import java.util.Optional;
public interface PasswordResetTokenService {
    PasswordResetToken createToken(User user);
    boolean isValidToken(String token);
    Optional<User> getUserByToken(String token);
    PasswordResetToken findByUser(User user);
    void deleteToken(String token);
}
