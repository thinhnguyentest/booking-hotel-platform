package com.booking_hotel.api.image.dto;

import com.booking_hotel.api.room.dto.RoomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {
    private Long imageId;
    private String imageUrl;
    private RoomResponse roomResponse;
}
