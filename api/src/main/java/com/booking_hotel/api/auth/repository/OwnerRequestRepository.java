package com.booking_hotel.api.auth.repository;

import com.booking_hotel.api.auth.entity.OwnerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRequestRepository extends JpaRepository<OwnerRequest, Long> {
}
