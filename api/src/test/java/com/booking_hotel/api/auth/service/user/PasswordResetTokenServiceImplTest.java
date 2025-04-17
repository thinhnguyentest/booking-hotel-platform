package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.PasswordResetTokenRepository;
import com.booking_hotel.api.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetTokenServiceImplTest {

    private PasswordResetTokenRepository tokenRepository;
    private UserRepository userRepository;
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(PasswordResetTokenRepository.class);
        userRepository = mock(UserRepository.class);
        passwordResetTokenService = new PasswordResetTokenServiceImpl(tokenRepository, userRepository);
    }

    @Test
    void testIsValidToken_ValidToken() {
        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setExpiryDate(ZonedDateTime.now().plusSeconds(60));

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        boolean isValid = passwordResetTokenService.isValidToken(tokenValue);

        assertThat(isValid).isTrue();
    }

    @Test
    void testIsValidToken_ExpiredToken() {
        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setExpiryDate(ZonedDateTime.now().minusSeconds(1));

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        boolean isValid = passwordResetTokenService.isValidToken(tokenValue);

        assertThat(isValid).isFalse();
    }

    @Test
    void testIsValidToken_NonExistentToken() {
        String tokenValue = UUID.randomUUID().toString();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        boolean isValid = passwordResetTokenService.isValidToken(tokenValue);

        assertThat(isValid).isFalse();
    }

    @Test
    void testGetUserByToken() {
        String tokenValue = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername("test_user");

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);
        token.setUser(user);

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        Optional<User> result = passwordResetTokenService.getUserByToken(tokenValue);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void testGetUserByToken_NonExistentToken() {
        String tokenValue = UUID.randomUUID().toString();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        Optional<User> result = passwordResetTokenService.getUserByToken(tokenValue);

        assertThat(result).isNotPresent();
    }

    @Test
    void testDeleteToken() {
        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenValue);

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.of(token));

        passwordResetTokenService.deleteToken(tokenValue);

        verify(tokenRepository).delete(token);
    }

    @Test
    void testDeleteToken_NonExistentToken() {
        String tokenValue = UUID.randomUUID().toString();

        when(tokenRepository.findByToken(tokenValue)).thenReturn(Optional.empty());

        passwordResetTokenService.deleteToken(tokenValue);

        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void testFindByUser() {
        User user = new User();
        user.setUsername("test_user");
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);

        when(tokenRepository.findPasswordResetTokenByUser(user)).thenReturn(token);

        PasswordResetToken result = passwordResetTokenService.findByUser(user);

        assertThat(result).isEqualTo(token);
    }
}
