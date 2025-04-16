package com.booking_hotel.api.auth.service.userDetails;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerUserDetailsServiceTest {

    private CustomerUserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new CustomerUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        String username = "john@example.com";
        User user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        when(userRepository.findByEmail(username)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("john_doe");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.getAuthorities()).isEmpty(); // No authorities assigned
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "unknown@example.com";

        when(userRepository.findByEmail(username)).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(username);
    }
}
