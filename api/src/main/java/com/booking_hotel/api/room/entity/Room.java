package com.booking_hotel.api.room.entity;


import com.booking_hotel.api.hotel.entity.Hotel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonBackReference
    private Hotel hotel;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(nullable = false)
    private Double price;


    @ElementCollection
    @CollectionTable(name = "room_unavailable_dates", joinColumns = @JoinColumn(name = "room_id"))
    private List<ZonedDateTime> unAvailableDates = new ArrayList<>();

    @Column(name = "is_available")
    @Builder.Default
    private Boolean isAvailable = true;
}

