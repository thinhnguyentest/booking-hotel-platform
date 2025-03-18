package com.booking_hotel.api.auth.service.owner;

import org.springframework.http.ResponseEntity;

public interface OwnerRequestService {
    ResponseEntity<?> signupBecomeOwner(String token);
    ResponseEntity<?> approveOwner(Long userId);
}
