package com.booking_hotel.api.review.dto;

import com.booking_hotel.api.auth.dto.UserResponse;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import com.booking_hotel.api.hotel.entity.Hotel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@Builder
public class ReviewResponse {
    private Long reviewId;
    private HotelResponse hotelResponse;
    private UserResponse userResponse;
    private Integer rating;
    private String comment;
    private ZonedDateTime createdAt;
}
