package com.booking.booking_room.entity.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.booking.Booking;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.enumerate.payment.PaymentMethod;
import com.booking.booking_room.enumerate.payment.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    @Column(nullable = false)
    private LocalDateTime paymentDate;
    @Column(name = "transaction_id")
    private String transactionId;
    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        this.paymentDate = LocalDateTime.now();
    }
}
