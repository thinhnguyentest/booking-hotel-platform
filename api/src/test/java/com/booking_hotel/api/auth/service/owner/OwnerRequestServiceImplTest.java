package com.booking_hotel.api.auth.service.owner;

import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.entity.OwnerRequest;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.OwnerRequestRepository;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.role.repository.RoleRepository;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerRequestServiceImplTest {

    private UserService userService;
    private OwnerRequestRepository ownerRequestRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private OwnerRequestServiceImpl ownerRequestService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        ownerRequestRepository = mock(OwnerRequestRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);

        ownerRequestService = new OwnerRequestServiceImpl(
                userService,
                ownerRequestRepository,
                roleRepository,
                userRepository
        );
    }

    @Test
    void testSignupBecomeOwner_UserNotFound() {
        String token = "invalid-token";
        String username = "invalid_user";

        mockStatic(JwtProvider.class).when(() -> JwtProvider.getUserNameByToken(token)).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerRequestService.signupBecomeOwner(token))
                .isInstanceOf(ElementNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testApproveOwner_Success() {
        Long userId = 1L;

        User user = User.builder()
                .username("john")
                .roles(new HashSet<>())
                .build();

        OwnerRequest request = OwnerRequest.builder()
                .user(user)
                .isApproved(false)
                .build();

        Role ownerRole = Role.builder()
                .roleName(RoleUtils.ROLE_OWNER)
                .build();

        when(ownerRequestRepository.findById(userId)).thenReturn(Optional.of(request));
        when(roleRepository.findRoleByRoleName(RoleUtils.ROLE_OWNER)).thenReturn(ownerRole);

        ResponseEntity<?> response = ownerRequestService.approveOwner(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Owner approved successfully");

        assertThat(user.getRoles()).contains(ownerRole);
        assertThat(request.isApproved()).isTrue();

        verify(userRepository).save(user);
        verify(ownerRequestRepository).save(request);
    }

    @Test
    void testApproveOwner_AlreadyApproved() {
        Long userId = 2L;
        OwnerRequest request = OwnerRequest.builder()
                .user(User.builder().username("already_owner").build())
                .isApproved(true)
                .build();

        when(ownerRequestRepository.findById(userId)).thenReturn(Optional.of(request));

        ResponseEntity<?> response = ownerRequestService.approveOwner(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Owner request is already approved");
    }

    @Test
    void testApproveOwner_NotFound() {
        Long userId = 99L;
        when(ownerRequestRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerRequestService.approveOwner(userId))
                .isInstanceOf(ElementNotFoundException.class)
                .hasMessage("Owner request not found");
    }
}
