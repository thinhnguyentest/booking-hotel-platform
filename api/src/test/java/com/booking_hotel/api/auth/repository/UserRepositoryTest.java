package com.booking_hotel.api.auth.repository;

import com.booking_hotel.api.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("johndoe");
        testUser.setEmail("john@example.com");
        testUser.setPassword("secret"); // giả định bạn có field này
        testUser.setRefreshToken("refresh-token-abc");

        userRepository.save(testUser);
    }

    @Test
    void testFindByEmail() {
        User found = userRepository.findByEmail("john@example.com");
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void testFindByUsername() {
        User found = userRepository.findUserByUsername("johndoe");
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testFindByRefreshToken() {
        User found = userRepository.findUserByRefreshToken("refresh-token-abc");
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void testFindByEmail_NotFound() {
        User found = userRepository.findByEmail("notfound@example.com");
        assertThat(found).isNull();
    }
}
