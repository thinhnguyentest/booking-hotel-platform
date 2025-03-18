package com.booking_hotel.api.auth.controller;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;
import static com.booking_hotel.api.utils.regexUtils.RegexUtils. *;
import static com.booking_hotel.api.utils.emailUtils.LinkSendMail. *;
import com.booking_hotel.api.auth.dto.AuthResponse;
import com.booking_hotel.api.auth.config.jwt.JwtProvider;
import com.booking_hotel.api.auth.dto.ResetPasswordRequest;
import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.auth.service.mail.EmailService;
import com.booking_hotel.api.auth.service.owner.OwnerRequestService;
import com.booking_hotel.api.auth.service.user.PasswordResetTokenService;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.auth.service.userDetails.CustomUserDetails;
import com.booking_hotel.api.exception.ElementNotFoundException;
import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.role.repository.RoleRepository;
import com.booking_hotel.api.utils.roleUtils.RoleUtils;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final PasswordResetTokenService tokenService;

    private final EmailService emailService;

    private final OwnerRequestService ownerRequestService;
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody User user) throws Exception {
        // Validate Username
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new Exception(USER_NAME_NULL);
        }
        if (user.getUsername().length() < 8 || user.getUsername().length() > 32) {
            throw new Exception(USER_NAME_RANGE);
        }
        User existingUserByUsername = userRepository.findUserByUsername(user.getUsername());
        if (existingUserByUsername != null) {
            throw new Exception(USER_NAME_EXISTED);
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
            throw new Exception(EMAIL_NULL);
        }
        if (!user.getEmail().matches(EMAIL_REGEX)) {
            throw new Exception(EMAIL_FORMAT);
        }
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null) {
            throw new Exception(EMAIL_EXISTED);
        }


        // Initial new user
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        // Handle role
        Set<Role> roles = new HashSet<>();

        Role defaultRole = roleRepository.findRoleByRoleName(RoleUtils.ROLE_USER);
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setRoleName(RoleUtils.ROLE_USER);
            roleRepository.save(defaultRole);
        }

        roles.add(defaultRole);
        newUser.setRoles(roles);

        // Save new user
        userRepository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        // find user by username
        User existingUser = userRepository.findUserByUsername(user.getUsername());

        if (existingUser == null || user.getUsername().trim().isEmpty()) {
            throw new Exception(USER_NOT_FOUND);
        }

        //check isBanned of existUser
        if (existingUser.getBannedUntil() != null && ZonedDateTime.now().isAfter(existingUser.getBannedUntil())) {
            // If the ban period has expired, set Banned False
            existingUser.setBanned(false);
            existingUser.setBannedUntil(null);
            userRepository.save(existingUser);
        } else if (existingUser.isBanned()) {
            // If the user is still banned, log error
            throw new Exception(BANNED_USER_MESSAGE);
        }

        // Check the times input account
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            if (existingUser.getFailedLoginAttempts() >= 4) {
                // ban user for 10 minutes
                existingUser.setBanned(true);
                existingUser.setBannedUntil(ZonedDateTime.now().plusMinutes(10));
                userRepository.save(existingUser);
                throw new Exception(BAN_USER_MESSAGE);
            } else {
                // Increase times of incorrect password entries
                existingUser.setFailedLoginAttempts(existingUser.getFailedLoginAttempts() + 1);
                userRepository.save(existingUser);
                throw new Exception(PASSWORD_INVALID);
            }
        } else {
            // Reset times of incorrect password entries
            existingUser.setFailedLoginAttempts(0);
            userRepository.save(existingUser);
        }

        // Initial Authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(existingUser.getUsername(), existingUser.getRoles());

        // Set Authentication inside SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Create refreshToken
        String refreshToken = UUID.randomUUID().toString(); // Tạo một Refresh Token ngẫu nhiên
        existingUser.setRefreshToken(refreshToken);
        userRepository.save(existingUser);

        // Create JWT token
        String jwt = JwtProvider.generateToken(existingUser.getUsername(), existingUser.getRoles());
        Set<Role> roles = existingUser.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        // Lưu token vào UserDetails
        CustomUserDetails customUserDetails = new CustomUserDetails(existingUser.getUsername(), existingUser.getPassword(), authorities);
        customUserDetails.setToken(jwt);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities()));

        // Create response
        AuthResponse response = AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) throws Exception {
        // Find user by refreshToken
        User existingUser = userRepository.findUserByRefreshToken(refreshToken);
        if (existingUser == null) {
            throw new Exception("Invalid refresh token");
        }

        // reset access token
        String newJwt = JwtProvider.generateToken(existingUser.getUsername(), existingUser.getRoles());

        // Tạo phản hồi mới
        AuthResponse response = AuthResponse.builder()
                .accessToken(newJwt)
                .refreshToken(refreshToken)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isPresent()) {
            PasswordResetToken tokenIxisted = tokenService.findByUser(user.get());
            if(tokenIxisted != null) {
                tokenService.deleteToken(tokenIxisted.getToken());
            }
            PasswordResetToken token = tokenService.createToken(user.get());
            String resetLink = RESET_LINK + token.getToken();
            try {
                emailService.sendEmail(email, PASSWORD_RESET, PASSWORD_DIRECT_RESET + resetLink + "\n " + PASSWORD_DIRECT_RESET_LIMIT);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok(PASSWORD_RESET_LINK_NOTIFICATION);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        // validate Password
        if (resetPasswordRequest.getNewPassword() == null || resetPasswordRequest.getNewPassword().trim().isEmpty()) {
            throw new IllegalArgumentException(PASSWORD_NULL);
        }
        if (resetPasswordRequest.getNewPassword().length() < 8 || resetPasswordRequest.getNewPassword().length() > 32) {
            throw new IllegalArgumentException(PASSWORD_RANGE);
        }
        if (!resetPasswordRequest.getNewPassword().matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException(PASSWORD_FORMAT);
        }



        if (tokenService.isValidToken(resetPasswordRequest.getToken())) {
            User user = tokenService.getUserByToken(resetPasswordRequest.getToken())
                    .orElseThrow(() -> new RuntimeException(TOKEN_INVALID));

            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword())); // Mã hóa mật khẩu trước khi lưu
            userRepository.save(user);
            tokenService.deleteToken(resetPasswordRequest.getToken());

            return ResponseEntity.ok(PASSWORD_CHANGE_SUCCESSFULLY);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TOKEN_INVALID);
    }

    @PostMapping("/signupBecomeOwner")
    public ResponseEntity<?> signupBecomeOwner(@RequestHeader("Authorization") String accessToken) {
        String token = accessToken.substring(7);
        return ownerRequestService.signupBecomeOwner(token);
    }

    @PostMapping("/approveOwner")
    public ResponseEntity<?> approveOwner(@RequestParam Long userId) {
        return ownerRequestService.approveOwner(userId);
    }

    @PostMapping("/unbanUser")
    public ResponseEntity<?> unbanUser(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ElementNotFoundException(USER_NOT_FOUND));
        user.setBanned(false);
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }


}
