package com.booking_hotel.api.role.entity;

import com.booking_hotel.api.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
@Builder
@Entity
@Data
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}

