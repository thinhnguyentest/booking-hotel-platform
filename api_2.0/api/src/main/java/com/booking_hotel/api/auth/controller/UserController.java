package com.booking_hotel.api.auth.controller;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.auth.repository.UserRepository;
import com.booking_hotel.api.auth.service.user.UserService;
import com.booking_hotel.api.exception.ElementNotFoundException;
import static com.booking_hotel.api.utils.messageUtils.MessageUtils. *;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            userRepository.delete(user.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ElementNotFoundException(USER_NOT_FOUND);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String accessToken, @RequestBody User user) {
        String token = accessToken.substring(7);
        return userService.updateUser(token, user);
    }

}
