package com.booking_hotel.api.auth.controller;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("123456");
        user = userRepository.save(user);
        testUserId = user.getUserId();
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("testuser");
    }

    @Test
    void testGetUserById() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/" + testUserId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("testuser");
    }

    @Test
    void testGetUserById_NotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/9999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteUser() {
        ResponseEntity<Void> response = restTemplate.exchange("/api/users/" + testUserId, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testUpdateUser_Unauthorized() {
        // Giả token Authorization nhưng không hợp lệ => userService.updateUser sẽ fail
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer faketoken123");

        User updateUser = new User();
        updateUser.setUsername("updatedUser");
        updateUser.setEmail("updated@example.com");

        HttpEntity<User> request = new HttpEntity<>(updateUser, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/users", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }
}
