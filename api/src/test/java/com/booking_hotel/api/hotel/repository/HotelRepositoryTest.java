package com.booking_hotel.api.hotel.repository;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.hotel.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;


    @Test
    void testFindAllWithPaging() {
        Page<Hotel> page = hotelRepository.findAll(PageRequest.of(0, 5));

        assertThat(page).isNotNull();
        assertThat(page.getContent()).hasSizeGreaterThanOrEqualTo(2);
        assertThat(page.getContent().get(0).getName()).isNotBlank();
    }

}
