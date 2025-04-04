package com.booking_hotel.api.utils.dtoUtils;

import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponseUtils {
        public static ReviewResponse buildReviewResponse(Review review) {

            return ReviewResponse.builder()
                    .userResponse(UserResponseUtils.buildUserResponse(review.getUser()))
                    .hotelResponse(HotelResponseUtils.buildHotelResponse(review.getHotel()))
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .reviewId(review.getReviewId())
                    .createdAt(review.getCreatedAt())
                    .build();

        }

        public static List<ReviewResponse> convertToReviewResponseList(List<Review> reviews) {
            List<ReviewResponse> ReviewResponseList = new ArrayList<>();
            for (Review review : reviews) {
                ReviewResponseList.add(buildReviewResponse(review));
            }
            return ReviewResponseList;
        }
}
