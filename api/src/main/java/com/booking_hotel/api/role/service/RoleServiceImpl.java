package com.booking_hotel.api.role.service;

import com.booking_hotel.api.role.entity.Role;
import com.booking_hotel.api.role.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}
