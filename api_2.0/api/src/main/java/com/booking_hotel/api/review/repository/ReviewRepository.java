package com.booking_hotel.api.review.repository;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findReviewsByUser(User user);
    List<Review> findReviewsByHotel(Hotel hotel);
}
