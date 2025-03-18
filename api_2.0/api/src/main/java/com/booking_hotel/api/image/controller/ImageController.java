package com.booking_hotel.api.image.controller;

import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;
import com.booking_hotel.api.image.service.ImageService;
import com.booking_hotel.api.utils.dtoUtils.ImageResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<ImageResponse> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/imageByRoom")
    public List<ImageResponse> getImagesByRoomId(@RequestParam Long roomId) {
        return imageService.getImagesByRoomId(roomId);
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("roomId") Long roomId) throws IOException {
        ImageResponse imageResponse = imageService.uploadImage(file, roomId);
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImageById(@PathVariable Long id) {
        ImageResponse imageResponse = imageService.getImageById(id);
        return ResponseEntity.ok(imageResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}

