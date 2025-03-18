package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.auth.dto.UserResponse;
import com.booking_hotel.api.auth.entity.User;

public class UserResponseUtils {
    public static UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();
    }
}
