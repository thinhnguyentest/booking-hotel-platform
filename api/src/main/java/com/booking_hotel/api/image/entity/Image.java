package com.booking_hotel.api.image.entity;


import com.booking_hotel.api.hotel.entity.Hotel;
import com.booking_hotel.api.room.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name =" image_url", nullable = false)
    private String imageUrl;
}

