package com.booking_hotel.api.image.service;

import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageResponse> getAllImages();
    ImageResponse uploadImage(MultipartFile file, Long roomId);
    ImageResponse getImageById(Long id);
    List<ImageResponse> getImagesByRoomId(Long roomId);
    void deleteImage(Long id);
}
