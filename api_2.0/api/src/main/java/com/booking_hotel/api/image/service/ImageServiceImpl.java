package com.booking_hotel.api.image.service;

import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.image.dto.ImageResponse;
import com.booking_hotel.api.image.entity.Image;
import com.booking_hotel.api.image.repository.ImageRepository;
import com.booking_hotel.api.room.dto.RoomResponse;
import com.booking_hotel.api.room.entity.Room;
import com.booking_hotel.api.room.service.RoomService;
import com.booking_hotel.api.utils.dtoUtils.ImageResponseUtils;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Cloudinary cloudinary;
    private final RoomService roomService;

    @Override
    public List<ImageResponse> getAllImages() {
        return ImageResponseUtils.convertToImageResponseList(imageRepository.findAll());
    }

    @Override
    public List<ImageResponse> getImagesByRoomId(Long roomId) {
        Optional<Room> roomOptional = roomService.getRoomById(roomId);
        if(roomOptional.isEmpty()) {
            throw new ElementNotFoundException("Room not found");
        }
        List<Image> imageList = imageRepository.findImagesByRoom(roomOptional.get());
        return ImageResponseUtils.convertToImageResponseList(imageList);
    }

    @Override
    public ImageResponse uploadImage(MultipartFile file, Long roomId) {
        Map<String, Object> uploadResult = new HashMap<>();
        Optional<Room> roomOptional = roomService.getRoomById(roomId);
        if(roomOptional.isEmpty()){
            throw new ElementNotFoundException("Room not found");
        }
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String imageUrl = uploadResult.get("secure_url").toString();

        Image image = Image.builder().imageUrl(imageUrl).room(roomOptional.get()).build();
        imageRepository.save(image);

        return ImageResponseUtils.buildImageResponse(image);
    }

    @Override
    public ImageResponse getImageById(Long id) {
        return ImageResponseUtils.buildImageResponse(imageRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
