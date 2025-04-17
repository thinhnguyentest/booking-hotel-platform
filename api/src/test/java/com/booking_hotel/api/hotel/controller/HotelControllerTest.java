package com.booking_hotel.api.hotel.controller;

import com.booking_hotel.api.ApiApplication;
import com.booking_hotel.api.hotel.dto.HotelResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApiApplication.class)
@TestPropertySource("classpath:application-test.properties")
class HotelControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetAllHotels() {
        ResponseEntity<List<HotelResponse>> response = restTemplate.exchange(
                "/api/hotels?page=0&size=5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testSearchHotelsWithNoFilter() {
        ResponseEntity<List<HotelResponse>> response = restTemplate.exchange(
                "/api/hotels/search",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCountByCity() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/hotels/countByCity", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
