package com.booking_hotel.api.review.controller;


import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;
import com.booking_hotel.api.review.service.ReviewService;
import com.booking_hotel.api.review.service.specifications.ReviewSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils.NOT_FOUND_HOTEL_MESSAGE;
import static com.booking_hotel.api.utils.messageUtils.MessageUtils.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final HotelService hotelService;
    private final UserService userService;

    @PostMapping
    public ReviewResponse createReview(@RequestBody Review review, @RequestParam Long hotelId, @CookieValue("access_token") String accessToken) {
        return reviewService.createReview(review, hotelId, accessToken);
    }

    @GetMapping("/{id}")
    public Optional<ReviewResponse> getReview(@RequestParam Long id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/hotelDetails")
    public List<ReviewResponse> getReviewsByHotel(@RequestParam Long hotelId) {
        return reviewService.getReviewsByHotel(hotelId);
    }

    @GetMapping("/userDetails")
    public List<ReviewResponse> getReviewsByUser(@CookieValue("access_token") String accessToken) {
        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(accessToken));
        if(userOptional.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        return reviewService.getReviewsByUser(userOptional.get());
    }

    @GetMapping
    public List<ReviewResponse> getAllReviews(
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String comment,
            Pageable pageable) {

        Specification<Review> spec = Specification.where(ReviewSpecification.hasRating(rating))
                .and(ReviewSpecification.hasCommentContaining(comment));

        return reviewService.getAllReviews(spec, pageable);
    }

    @PutMapping()
    public ReviewResponse updateReview(@RequestParam Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }
}

