package com.booking_hotel.api.auth.repository;

import com.booking_hotel.api.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findUserByUsername(String username);
}
