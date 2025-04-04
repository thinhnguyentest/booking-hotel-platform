package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;
import static com.booking_hotel.api.utils.regexUtils.RegexUtils.EMAIL_REGEX;
import static com.booking_hotel.api.utils.regexUtils.RegexUtils.PASSWORD_REGEX;

import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.role.repository.RoleRepository;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findUserByUsername(username));
    }

    @Override
    public ResponseEntity<?> updateUser(String token, User user) {
        Optional<User> userOptional = findByUsername(JwtProvider.getUserNameByToken(token));
        if(userOptional.isPresent()) {
            User userToUpdate = userOptional.get();

            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException(USER_NAME_NULL);
            }
            if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
                throw new IllegalArgumentException(USER_NAME_RANGE);
            }

            // validate Password
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException(PASSWORD_NULL);
            }
            if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
                throw new IllegalArgumentException(PASSWORD_RANGE);
            }
            if (!user.getPassword().matches(PASSWORD_REGEX)) {
                throw new IllegalArgumentException(PASSWORD_FORMAT);
            }

            // Validate Email
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException(EMAIL_NULL);
            }
            if (!user.getEmail().matches(EMAIL_REGEX)) {
                throw new IllegalArgumentException(EMAIL_FORMAT);
            }
            User existingUserByEmail = userRepository.findByEmail(user.getEmail());
            if (existingUserByEmail != null) {
                throw new IllegalArgumentException(EMAIL_EXISTED);
            }

            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userToUpdate);

            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
}
