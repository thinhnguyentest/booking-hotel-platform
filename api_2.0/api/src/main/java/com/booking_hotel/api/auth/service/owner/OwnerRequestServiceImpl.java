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
import com.booking_hotel.api.utils.messageUtils.MessageUtils;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OwnerRequestServiceImpl implements OwnerRequestService {

    private final UserService userService;

    private final OwnerRequestRepository ownerRequestRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> signupBecomeOwner(String token) {
        Optional<User> userOptional = userService.findByUsername(JwtProvider.getUserNameByToken(token));
        if(userOptional.isPresent()) {
            User newUser = userOptional.get();
            OwnerRequest ownerRequest = OwnerRequest.builder()
                    .user(newUser)
                    .isApproved(false)
                    .build();
            ownerRequestRepository.save(ownerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            throw new ElementNotFoundException(MessageUtils.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> approveOwner(Long userId) {
        OwnerRequest ownerRequest = ownerRequestRepository.findById(userId)
                .orElseThrow(() -> new ElementNotFoundException("Owner request not found"));

        if (!ownerRequest.isApproved()) {
            User user = ownerRequest.getUser();

            Set<Role> roles = user.getRoles();
            Role ownerRole = roleRepository.findRoleByRoleName(RoleUtils.ROLE_OWNER);
            if (ownerRole == null) {
                ownerRole = Role.builder().roleName(RoleUtils.ROLE_OWNER).build();
                roleRepository.save(ownerRole);
            }
            roles.add(ownerRole);
            user.setRoles(roles);

            ownerRequest.setApproved(true);
            userRepository.save(user);
            ownerRequestRepository.save(ownerRequest);
            return ResponseEntity.ok("Owner approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Owner request is already approved");
        }
    }
}
