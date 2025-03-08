package com.booking_hotel.api.auth.controller;

import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;
import static com.booking_hotel.api.utils.regexUtils.RegexUtils. *;
import static com.booking_hotel.api.utils.roleUtils.RoleUtils. *;
import static com.booking_hotel.api.utils.emailUtils.LinkSendMail. *;
import com.booking_hotel.api.auth.dto.AuthResponse;
import com.booking_hotel.api.auth.config.JwtProvider;
import com.booking_hotel.api.auth.dto.ResetPasswordRequest;
import com.booking_hotel.api.auth.entity.PasswordResetToken;
import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.auth.service.EmailService;
import com.booking_hotel.api.auth.service.PasswordResetTokenService;
import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.role.repository.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
            throw new Exception(PASSWORD_NULL);
        }
        if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
            throw new Exception(PASSWORD_RANGE);
        }
        if (!user.getPassword().matches(PASSWORD_REGEX)) {
            throw new Exception(PASSWORD_FORMAT);
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
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            // Initial default role ("customer")
            Role defaultRole = roleRepository.findRoleByRoleName(CUSTOMER);
            if (defaultRole == null) {
                defaultRole = new Role();
                defaultRole.setRoleName(CUSTOMER);
                roleRepository.save(defaultRole); // Lưu vai trò mặc định nếu chưa tồn tại
            }
            roles.add(defaultRole);
        } else {
            // Check and save role
            for (Role role : user.getRoles()) {
                Role existingRole = roleRepository.findRoleByRoleName(role.getRoleName());
                if (existingRole == null) {
                    roleRepository.save(role);
                }
                roles.add(existingRole != null ? existingRole : role);
            }
        }
        newUser.setRoles(roles);

        // Save new user
        userRepository.save(newUser);

        // Create JWT token
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        String jwt = JwtProvider.generateToken(auth);

        // Response
        AuthResponse response = AuthResponse.builder()
                                        .jwt(jwt)
                                        .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        // Tìm người dùng theo username
        User existingUser = userRepository.findUserByUsername(user.getUsername());

        if (existingUser.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new Exception(USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new Exception(PASSWORD_INVALID);
        }

        // Initial Authentication
        Authentication auth = new UsernamePasswordAuthenticationToken
                (existingUser.getUsername(), existingUser.getPassword());

        // Set Authentication inside SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Create token JWT
        String jwt = JwtProvider.generateToken(auth);

        // Create response
        AuthResponse response = AuthResponse.builder()
                                            .jwt(jwt)
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
        if (tokenService.isValidToken(resetPasswordRequest.getToken())) {
            User user = tokenService.getUserByToken(resetPasswordRequest.getToken()).orElseThrow(() -> new RuntimeException("Invalid token"));
            user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword())); // Mã hóa mật khẩu trước khi lưu
            userRepository.save(user);
            tokenService.deleteToken(resetPasswordRequest.getToken());

            return ResponseEntity.ok(PASSWORD_CHANGE_SUCCESSFULLY);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TOKEN_INVALID);
    }

}
