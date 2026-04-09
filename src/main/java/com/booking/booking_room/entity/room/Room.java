package com.booking.booking_room.entity.room;

import java.math.BigDecimal;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.information.Information;
import com.booking.booking_room.enumarate.room.RoomStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.booking.booking_room.entity.booking.Booking;
import com.booking.booking_room.entity.booking.Review;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "information_id")
    private Information information;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Column(nullable = false)
    private String roomNumber;
    @Column(nullable = false)
    private Integer floor;
    @Column(nullable = false)
    private RoomStatus roomStatus = RoomStatus.AVAILABLE;
    @Column(name = "last_cleaned_at")
    @Builder.Default
    private LocalDateTime lastCleanedAt = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal price;

    @OneToOne(mappedBy = "room")
    private RoomPolicy roomPolicy;
    @OneToOne(mappedBy = "room")
    private RoomAmenity roomAmenity;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "room")
    private List<Review> reviews;

    @PrePersist
    public void prePersist() {
        if (this.lastCleanedAt == null) {
            this.lastCleanedAt = LocalDateTime.now();
        }
        if (this.roomStatus == null) {
            this.roomStatus = RoomStatus.AVAILABLE;
        }
    }
}
