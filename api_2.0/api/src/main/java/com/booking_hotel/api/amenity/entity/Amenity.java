package com.booking_hotel.api.amenity.entity;

import com.booking_hotel.api.hotel.entity.Hotel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id", nullable = false)
    private Long amenityId;

    private String amenityName;

    private String amenityDescription;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}
