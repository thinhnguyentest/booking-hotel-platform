package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.PasswordResetTokenRepository;
import com.booking_hotel.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    private final UserRepository userRepository;

    public PasswordResetToken createToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(ZonedDateTime.now().plusSeconds(60));
        return tokenRepository.save(token);
    }

    public boolean isValidToken(String token) {
        System.out.println(token);
        return tokenRepository.findByToken(token)
                .map(t -> t.getExpiryDate().isAfter(ZonedDateTime.now()))
                .orElse(false);
    }

    public Optional<User> getUserByToken(String token) {
        return tokenRepository.findByToken(token).map(PasswordResetToken::getUser);
    }

    @Override
    public void deleteToken(String token) {
        tokenRepository.findByToken(token).ifPresent(tokenRepository::delete);
    }

    @Override
    public PasswordResetToken findByUser(User user) {
        return tokenRepository.findPasswordResetTokenByUser(user);
    }
}