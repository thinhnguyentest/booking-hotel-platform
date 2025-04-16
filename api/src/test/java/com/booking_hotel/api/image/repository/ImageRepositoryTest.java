package com.booking_hotel.api.image.repository;

import com.booking_hotel.api.image.entity.Image;
import com.booking_hotel.api.room.entity.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        // Tạo và lưu một Room mới trước mỗi bài kiểm tra
        room = new Room();
        room.setRoomId(1L);
        room.setRoomType("Deluxe Room");
        // Thêm các thuộc tính khác nếu cần thiết
    }

    @Test
    void testFindImagesByRoom() {
        // Tạo và lưu một số hình ảnh cho phòng
        Image image1 = new Image();
        image1.setImageId(1L);
        image1.setImageUrl("http://example.com/image1.jpg");
        image1.setRoom(room);
        imageRepository.save(image1);

        Image image2 = new Image();
        image2.setImageId(2L);
        image2.setImageUrl("http://example.com/image2.jpg");
        image2.setRoom(room);
        imageRepository.save(image2);

        // Gọi phương thức findImagesByRoom
        List<Image> images = imageRepository.findImagesByRoom(room);

        // Kiểm tra kết quả
        assertEquals(2, images.size());
        assertTrue(images.stream().anyMatch(image -> image.getImageUrl().equals("http://example.com/image1.jpg")));
        assertTrue(images.stream().anyMatch(image -> image.getImageUrl().equals("http://example.com/image2.jpg")));
    }
}
