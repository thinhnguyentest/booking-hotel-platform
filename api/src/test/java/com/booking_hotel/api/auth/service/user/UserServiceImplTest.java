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
    void testUpdateUser_Success() {
        String token = "mock-token";
        String username = "john_doe";
        User existingUser = new User();
        existingUser.setUsername(username);
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("oldPassword");

        when(JwtProvider.getUserNameByToken(token)).thenReturn(username);
        when(userRepository.findUserByUsername(username)).thenReturn(existingUser);

        User updatedUser = new User();
        updatedUser.setUsername("john_doe_updated");
        updatedUser.setEmail("john_updated@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedPassword");

        ResponseEntity<?> response = userService.updateUser(token, updatedUser);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(existingUser.getUsername()).isEqualTo("john_doe_updated");
        assertThat(existingUser.getEmail()).isEqualTo("john_updated@example.com");
        assertThat(existingUser.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        String token = "mock-token";
        String username = "invalid_user";

        when(JwtProvider.getUserNameByToken(token)).thenReturn(username);
        when(userRepository.findUserByUsername(username)).thenReturn(null);

        assertThatThrownBy(() -> userService.updateUser(token, new User()))
                .isInstanceOf(ElementNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testUpdateUser_UsernameNull() {
        String token = "mock-token";
        String username = "john_doe";
        User existingUser = new User();
        existingUser.setUsername(username);

        when(JwtProvider.getUserNameByToken(token)).thenReturn(username);
        when(userRepository.findUserByUsername(username)).thenReturn(existingUser);

        User updatedUser = new User();
        updatedUser.setUsername(null); // Null username

        assertThatThrownBy(() -> userService.updateUser(token, updatedUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null");
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

    @Test
    void testFindById_UserNotExists() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(ElementNotFoundException.class)
                .hasMessage("User not found");
    }
}
