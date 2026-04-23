package com.booking.booking_room.entity.otp;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTPInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime generateAt;

    private LocalDateTime expiredAt;
}
