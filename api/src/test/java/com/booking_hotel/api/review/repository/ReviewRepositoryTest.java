package com.booking_hotel.api.review.repository;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.review.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    private User user;
    private Hotel hotel;
    private Review review;

    @BeforeEach
    void setUp() {
        // Tạo và lưu người dùng, khách sạn và đánh giá vào cơ sở dữ liệu
        user = new User();
        user.setUsername("testUser");
        // Giả sử bạn đã có service để lưu user
        // userService.save(user);

        hotel = new Hotel();
        hotel.setName("Test Hotel");
        // Giả sử bạn đã có service để lưu hotel
        // hotelService.save(hotel);

        review = new Review();
        review.setComment("Great stay!");
        review.setRating(5);
        review.setUser(user);
        review.setHotel(hotel);
        reviewRepository.save(review);
    }

    @Test
    void findReviewsByUser_Success() {
        List<Review> reviews = reviewRepository.findReviewsByUser(user);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Great stay!", reviews.get(0).getComment());
    }

    @Test
    void findReviewsByHotel_Success() {
        List<Review> reviews = reviewRepository.findReviewsByHotel(hotel);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Great stay!", reviews.get(0).getComment());
    }
}
