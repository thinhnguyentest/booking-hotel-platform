package com.booking_hotel.api.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}
