package com.booking_hotel.api.hotel.entity;

import com.booking_hotel.api.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Builder
@Data
@Entity
//@Table(name = "Hotels", uniqueConstraints = {
//        @UniqueConstraint(columnNames = "name"),
//        @UniqueConstraint(columnNames = "owner_id")
//})
@Table(name = "Hotels")
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

//    @NotBlank(message = "Tên khách sạn không được để trống")
//    @Size(max = 100, message = "Tên khách sạn không được vượt quá 100 ký tự")
    @Column(nullable = false, unique = true)
    private String name;

//    @NotBlank(message = "Địa chỉ không được để trống")
//    @Size(max = 200, message = "Địa chỉ không được vượt quá 200 ký tự")
    @Column(nullable = false)
    private String address;

//    @NotBlank(message = "Thành phố không được để trống")
//    @Size(max = 100, message = "Thành phố không được vượt quá 100 ký tự")
    @Column(nullable = false)
    private String city;

//    @NotBlank(message = "Quốc gia không được để trống")
//    @Size(max = 100, message = "Quốc gia không được vượt quá 100 ký tự")
    @Column(nullable = false)
    private String country;

//    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}
