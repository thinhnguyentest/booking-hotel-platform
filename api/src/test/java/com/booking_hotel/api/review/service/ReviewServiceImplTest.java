package com.booking_hotel.api.review.service;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.hotel.repository.HotelRepository;
import com.booking_hotel.api.hotel.service.HotelService;
import com.booking_hotel.api.review.dto.ReviewResponse;
import com.booking_hotel.api.review.entity.Review;
import com.booking_hotel.api.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserService userService;

    @Mock
    private HotelService hotelService;

    @Mock
    private HotelRepository hotelRepository;

    private User user;
    private Hotel hotel;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");

        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setName("Test Hotel");

        review = new Review();
        review.setRating(5);
        review.setComment("Great stay!");
        review.setUser(user);
        review.setHotel(hotel);
    }

    @Test
    void createReview_UserNotFound() {
        when(userService.findByUsername(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ElementNotFoundException.class, () ->
                reviewService.createReview(review, hotel.getHotelId(), "dummyToken"));

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateReview_Success() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any())).thenReturn(review);

        ReviewResponse response = reviewService.updateReview(1L, review);

        assertNotNull(response);
        assertEquals("Great stay!", response.getComment());
        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void getReview_Success() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        Optional<ReviewResponse> response = reviewService.getReview(1L);

        assertTrue(response.isPresent());
        assertEquals("Great stay!", response.get().getComment());
    }

}
