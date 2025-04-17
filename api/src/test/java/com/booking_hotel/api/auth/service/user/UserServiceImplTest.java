package com.booking_hotel.api.auth.service.user;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder, roleRepository);
    }

    @Test
    void testFindByUsername_UserExists() {
        String username = "john_doe";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findUserByUsername(username)).thenReturn(user);

        Optional<User> result = userService.findByUsername(username);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testFindByUsername_UserNotExists() {
        String username = "invalid_user";

        when(userRepository.findUserByUsername(username)).thenReturn(null);

        Optional<User> result = userService.findByUsername(username);

        assertThat(result).isNotPresent();
    }

    @Test
    void testFindById_UserExists() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
    }

}
