package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.auth.dto.UserResponse;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;

import java.util.ArrayList;
import java.util.List;

public class UserResponseUtils {
    public static UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isBanned(user.isBanned())
                .build();
    }

    public static List<UserResponse> convertToUserResponseList(List<User> users) {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : users) {
            userResponseList.add(buildUserResponse(user));
        }
        return userResponseList;
    }
}
