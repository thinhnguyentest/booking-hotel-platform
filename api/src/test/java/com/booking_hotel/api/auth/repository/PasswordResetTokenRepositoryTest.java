package com.booking_hotel.api.auth.repository;

import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        passwordResetTokenRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("resetuser");
        testUser.setEmail("reset@example.com");
        testUser.setPassword("123456");
        userRepository.save(testUser);

        token = new PasswordResetToken();
        token.setToken("abc123token");
        token.setUser(testUser);
        token.setExpiryDate(ZonedDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(token);
    }

    @Test
    void testFindByToken() {
        Optional<PasswordResetToken> result = passwordResetTokenRepository.findByToken("abc123token");
        assertThat(result).isPresent();
        assertThat(result.get().getUser().getUsername()).isEqualTo("resetuser");
    }

    @Test
    void testFindByUser() {
        PasswordResetToken result = passwordResetTokenRepository.findPasswordResetTokenByUser(testUser);
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("abc123token");
    }

    @Test
    void testTokenList() {
        List<PasswordResetToken> tokens = passwordResetTokenRepository.token("abc123token");
        assertThat(tokens).isNotEmpty();
        assertThat(tokens.get(0).getUser().getEmail()).isEqualTo("reset@example.com");
    }

    @Test
    void testFindByToken_NotFound() {
        Optional<PasswordResetToken> result = passwordResetTokenRepository.findByToken("wrong-token");
        assertThat(result).isEmpty();
    }
}
