package com.booking.booking_room.entity.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.payment.Payment;
import com.booking.booking_room.entity.room.Room;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.enumerate.booking.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(unique = true)
    private String bookingCode;
    @Column(nullable = false)
    private LocalDateTime checkIn;
    @Column(nullable = false)
    private LocalDateTime checkOut;
    private Integer numAdults;
    private Integer numChildren;
    @Column(nullable = false)
    private BigDecimal totalPrice;
    @Column(nullable = false)
    private BookingStatus status;
    @OneToMany(mappedBy = "booking")
    private List<Payment> payments;
    @Builder.Default
    private LocalDateTime dateBook = LocalDateTime.now();
    @OneToOne(mappedBy = "booking")
    private BookingGuest bookingGuest;
    @OneToOne(mappedBy = "booking")
    private BookingNote bookingNote;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @OneToOne(mappedBy = "booking")
    private Cancellation cancellation;
    @OneToMany(mappedBy = "booking")
    private List<Review> reviews;
}
