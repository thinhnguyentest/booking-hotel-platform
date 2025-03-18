package com.booking_hotel.api.review.service.specifications;

import com.booking_hotel.api.review.entity.Review;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> hasRating(Integer rating) {
        return (root, query, criteriaBuilder) -> {
            if (rating == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("rating"), rating);
        };
    }

    public static Specification<Review> hasCommentContaining(String comment) {
        return (root, query, criteriaBuilder) -> {
            if (comment == null || comment.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("comment"), "%" + comment + "%");
        };
    }
}

