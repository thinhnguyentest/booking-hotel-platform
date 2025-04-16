package com.booking_hotel.api.review.controller;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;
import com.booking_hotel.api.review.service.ReviewService;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_Success() {
        Review review = new Review(); // Set các thuộc tính cần thiết
        ReviewResponse reviewResponse = ReviewResponse.builder().build(); // Set các thuộc tính cần thiết
        when(reviewService.createReview(any(Review.class), anyLong(), anyString())).thenReturn(reviewResponse);

        ReviewResponse response = reviewController.createReview(review, 1L, "valid_token");

        assertNotNull(response);
        verify(reviewService, times(1)).createReview(any(Review.class), anyLong(), anyString());
    }

    @Test
    void getReview_Success() {
        ReviewResponse reviewResponse = ReviewResponse.builder().build(); // Set các thuộc tính cần thiết
        when(reviewService.getReview(anyLong())).thenReturn(Optional.of(reviewResponse));

        Optional<ReviewResponse> response = reviewController.getReview(1L);

        assertTrue(response.isPresent());
        assertEquals(reviewResponse, response.get());
    }

    @Test
    void getReview_NotFound() {
        when(reviewService.getReview(anyLong())).thenReturn(Optional.empty());

        Optional<ReviewResponse> response = reviewController.getReview(1L);

        assertFalse(response.isPresent());
    }

    @Test
    void getReviewsByHotel_Success() {
        when(reviewService.getReviewsByHotel(anyLong())).thenReturn(Collections.emptyList());

        List<ReviewResponse> response = reviewController.getReviewsByHotel(1L);

        assertNotNull(response);
        verify(reviewService, times(1)).getReviewsByHotel(anyLong());
    }

    @Test
    void getReviewsByUser_Success() {
        String token = "valid_token";
        User user = new User(); // Set các thuộc tính cần thiết
        ReviewResponse reviewResponse = ReviewResponse.builder().build(); // Set các thuộc tính cần thiết
        when(userService.findByUsername(JwtProvider.getUserNameByToken(token))).thenReturn(Optional.of(user));
        when(reviewService.getReviewsByUser(user)).thenReturn(Collections.singletonList(reviewResponse));

        List<ReviewResponse> response = reviewController.getReviewsByUser(token);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(reviewService, times(1)).getReviewsByUser(user);
    }

    @Test
    void getReviewsByUser_UserNotFound() {
        String token = "invalid_token";
        when(userService.findByUsername(JwtProvider.getUserNameByToken(token))).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> reviewController.getReviewsByUser(token));
    }

    @Test
    void getAllReviews_Success() {
        when(reviewService.getAllReviews(any(), any())).thenReturn(Collections.emptyList());

        List<ReviewResponse> response = reviewController.getAllReviews(5, "test", PageRequest.of(0, 10));

        assertNotNull(response);
        verify(reviewService, times(1)).getAllReviews(any(), any());
    }

    @Test
    void updateReview_Success() {
        Review review = new Review(); // Set các thuộc tính cần thiết
        ReviewResponse reviewResponse = ReviewResponse.builder().build(); // Set các thuộc tính cần thiết
        when(reviewService.updateReview(anyLong(), any(Review.class))).thenReturn(reviewResponse);

        ReviewResponse response = reviewController.updateReview(1L, review);

        assertNotNull(response);
        verify(reviewService, times(1)).updateReview(anyLong(), any(Review.class));
    }

    @Test
    void deleteReview_Success() {
        doNothing().when(reviewService).deleteReview(anyLong());

        reviewController.deleteReview(1L);

        verify(reviewService, times(1)).deleteReview(anyLong());
    }
}
