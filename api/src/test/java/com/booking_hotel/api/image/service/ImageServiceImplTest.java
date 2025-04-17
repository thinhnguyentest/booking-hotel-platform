package com.booking_hotel.api.image.service;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;
import com.booking_hotel.api.image.repository.ImageRepository;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.dtoUtils.ImageResponseUtils;
import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private RoomService roomService;

    @Mock
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    private Room room;
    private Image image;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        room = new Room();
        room.setRoomId(1L);
        image = new Image();
        image.setImageId(1L);
        image.setRoom(room);
        image.setImageUrl("http://example.com/image.jpg");
    }

    @Test
    void testGetAllImages() {
        when(imageRepository.findAll()).thenReturn(Collections.singletonList(image));
        List<ImageResponse> responses = imageService.getAllImages();
        assertEquals(1, responses.size());
    }

    @Test
    void testGetImagesByRoomId_RoomNotFound() {
        when(roomService.getRoomById(1L)).thenReturn(Optional.empty());
        assertThrows(ElementNotFoundException.class, () -> imageService.getImagesByRoomId(1L));
    }

    @Test
    void testGetImagesByRoomId_Success() {
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));
        when(imageRepository.findImagesByRoom(room)).thenReturn(Collections.singletonList(image));
        List<ImageResponse> responses = imageService.getImagesByRoomId(1L);
        assertEquals(1, responses.size());
    }

    @Test
    void testUploadImage_RoomNotFound() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(roomService.getRoomById(1L)).thenReturn(Optional.empty());
        assertThrows(ElementNotFoundException.class, () -> imageService.uploadImage(file, 1L));
    }

    @Test
    void testGetImageById() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        ImageResponse response = imageService.getImageById(1L);
        assertNotNull(response);
    }

    @Test
    void testDeleteImage() {
        imageService.deleteImage(1L);
        verify(imageRepository).deleteById(1L);
    }
}
