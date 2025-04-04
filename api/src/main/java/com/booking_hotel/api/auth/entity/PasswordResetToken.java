package com.booking_hotel.api.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private ZonedDateTime expiryDate;

}

