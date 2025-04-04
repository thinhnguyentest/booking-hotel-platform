package com.booking_hotel.api.role.repository;

import com.booking_hotel.api.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByRoleName(String roleName);
}
