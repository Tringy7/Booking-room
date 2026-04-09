package com.booking.booking_room.entity.booking;

import java.time.LocalDateTime;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.information.Information;
import com.booking.booking_room.entity.room.Room;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.enumarate.booking.ReviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "information_id", nullable = true)
    private Information information;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String title;

    @Column(length = 4000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private Integer helpfulCount;

    @Column(nullable = false)
    private Integer unhelpfulCount;

    private String response;

    private LocalDateTime responseDate;
}
