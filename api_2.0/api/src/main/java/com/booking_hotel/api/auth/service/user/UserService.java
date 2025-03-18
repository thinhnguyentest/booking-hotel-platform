package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    ResponseEntity<?> updateUser(String token, User user);
    User findById(Long id);
}
