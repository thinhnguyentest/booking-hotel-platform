package com.booking_hotel.api.review.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    void deleteReview(Long id);
    ReviewResponse updateReview(Long id, Review reviewDetails);
    List<ReviewResponse> getAllReviews(Specification<Review> spec, Pageable pageable);
    List<ReviewResponse> getReviewsByUser(User user);
    List<ReviewResponse> getReviewsByHotel(Long hotelId);
    Optional<ReviewResponse> getReview(Long id);
    ReviewResponse createReview(Review review, Long hotelId, String token);
}
