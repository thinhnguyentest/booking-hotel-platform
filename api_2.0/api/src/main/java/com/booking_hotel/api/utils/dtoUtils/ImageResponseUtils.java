package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageResponseUtils {
    public static ImageResponse buildImageResponse(Image image) {

        return ImageResponse.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .roomResponse(RoomResponseUtils.buildRoomResponse(image.getRoom()))
                .build();
    }

    public static List<ImageResponse> convertToImageResponseList(List<Image> images) {
        List<ImageResponse> ImageResponseList = new ArrayList<>();
        for (Image image : images) {
            ImageResponseList.add(buildImageResponse(image));
        }
        return ImageResponseList;
    }
}
