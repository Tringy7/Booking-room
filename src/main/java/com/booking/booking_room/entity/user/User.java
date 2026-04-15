package com.booking.booking_room.entity.user;

import java.util.List;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.booking.Booking;
import com.booking.booking_room.entity.booking.Review;
import com.booking.booking_room.entity.payment.Payment;
import com.booking.booking_room.enumerate.user.UserRole;
import com.booking.booking_room.enumerate.user.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @Column(nullable = false, length = 1000)
    private String refreshToken;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToOne(mappedBy = "user")
    private UserProfile userProfile;
    @OneToOne(mappedBy = "user")
    private UserPreference userPreference;
    @OneToMany(mappedBy = "user")
    private List<Booking> booking;
    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlists;
    @OneToMany(mappedBy = "reviewer")
    private List<Review> reviews;
    @OneToMany(mappedBy = "user")
    private List<Payment> payments;
}
