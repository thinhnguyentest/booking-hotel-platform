package com.booking_hotel.api.booking.entity;

import com.booking_hotel.api.auth.entity.User;
import com.booking_hotel.api.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "check_in_date", nullable = false)
    private ZonedDateTime checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private ZonedDateTime checkOutDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    private String status;

    @CreationTimestamp
    private ZonedDateTime createdAt;
}

