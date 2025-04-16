package com.booking_hotel.api.image.controller;

import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private ImageResponse imageResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageResponse = new ImageResponse();
        imageResponse.setImageId(1L);
        imageResponse.setImageUrl("http://example.com/image.jpg");
    }

    @Test
    void testGetAllImages() {
        when(imageService.getAllImages()).thenReturn(Collections.singletonList(imageResponse));

        List<ImageResponse> response = imageController.getAllImages();

        assertEquals(1, response.size());
        assertEquals("http://example.com/image.jpg", response.get(0).getImageUrl());
    }

    @Test
    void testGetImagesByRoomId() {
        when(imageService.getImagesByRoomId(any(Long.class))).thenReturn(Collections.singletonList(imageResponse));

        List<ImageResponse> response = imageController.getImagesByRoomId(1L);

        assertEquals(1, response.size());
        assertEquals("http://example.com/image.jpg", response.get(0).getImageUrl());
    }

    @Test
    void testUploadImage() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(imageService.uploadImage(any(MultipartFile.class), any(Long.class))).thenReturn(imageResponse);

        ResponseEntity<ImageResponse> response = imageController.uploadImage(file, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(imageResponse, response.getBody());
    }

    @Test
    void testUploadHotelImage() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        HotelResponse hotelResponse = new HotelResponse(); // Tạo đối tượng HotelResponse nếu cần
        when(imageService.uploadHotelImage(any(MultipartFile.class), any(Long.class))).thenReturn(hotelResponse);

        ResponseEntity<HotelResponse> response = imageController.uploadHotelImage(file, 1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(hotelResponse, response.getBody());
    }

    @Test
    void testGetImageById() {
        when(imageService.getImageById(any(Long.class))).thenReturn(imageResponse);

        ResponseEntity<ImageResponse> response = imageController.getImageById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(imageResponse, response.getBody());
    }

    @Test
    void testDeleteImage() {
        doNothing().when(imageService).deleteImage(any(Long.class));

        ResponseEntity<Void> response = imageController.deleteImage(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(imageService, times(1)).deleteImage(1L);
    }
}
