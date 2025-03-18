package com.booking_hotel.api.review.service;

import static com.booking_hotel.api.utils.dtoUtils.ReviewResponseUtils. *;
import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;
import com.booking_hotel.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HotelService hotelService;
    private final HotelRepository hotelRepository;


    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewResponse updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        return buildReviewResponse(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponse> getAllReviews(Specification<Review> spec, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(spec, pageable);
        return convertToReviewResponseList(reviews.getContent());
    }

    @Override
    public Optional<ReviewResponse> getReview(Long id) {
        return Optional.of(buildReviewResponse(reviewRepository.findById(id).orElseThrow()));
    }

    @Override
    public ReviewResponse createReview(Review review, Long hotelId, String token) {
        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(userOptional.isEmpty()) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
        if(hotelOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_HOTEL_MESSAGE);
        }

        Review newReview = Review.builder()
                .hotel(hotelOptional.get())
                .user(userOptional.get())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();

        reviewRepository.save(newReview);
        List<ReviewResponse> reviewResponseList = getReviewsByHotel(hotelOptional.get().getHotelId());
        BigDecimal averageRating = reviewResponseList.stream()
                .map(ReviewResponse::getRating)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(reviewResponseList.size()), 1, RoundingMode.HALF_UP);

        Hotel hotel = hotelOptional.get();
        hotel.setRating(averageRating.doubleValue());
        hotelRepository.save(hotel);

        return buildReviewResponse(reviewRepository.save(newReview));
    }

    @Override
    public List<ReviewResponse> getReviewsByHotel(Long hotelId) {
        Optional<Hotel> hotelOptional = hotelService.getHotelById(hotelId);
        if(hotelOptional.isEmpty()) {
            throw new ElementNotFoundException(NOT_FOUND_HOTEL_MESSAGE);
        }
        return convertToReviewResponseList(reviewRepository.findReviewsByHotel(hotelOptional.get()));
    }


    @Override
    public List<ReviewResponse> getReviewsByUser(User user) {
        if(user == null) {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
        return convertToReviewResponseList(reviewRepository.findReviewsByUser(user));
    }
}
