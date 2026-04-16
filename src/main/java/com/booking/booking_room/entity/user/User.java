package com.booking.booking_room.entity.user;

import java.util.List;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.booking.Booking;
import com.booking.booking_room.entity.booking.Review;
import com.booking.booking_room.entity.payment.Payment;
import com.booking.booking_room.enumerate.user.UserRole;
import com.booking.booking_room.enumerate.user.UserStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false, length = 1000)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(length = 1000)
    @JsonIgnore
    private String refreshToken;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;;
    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;
    @OneToOne(mappedBy = "user")
    private UserPreference userPreference;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Booking> booking;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Wishlist> wishlists;
    @OneToMany(mappedBy = "reviewer")
    @JsonIgnore
    private List<Review> reviews;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Payment> payments;
}
