package com.booking.booking_room.entity.booking;

import java.time.LocalDateTime;
import java.util.List;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.enumerate.booking.CancellationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "cancellations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cancellation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(nullable = false)
    private LocalDateTime cancellationDate;

    @Column(nullable = false, length = 500)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "cancellation_policy_id", nullable = false)
    private CancellationPolicy cancellationPolicy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CancellationStatus status;

    @OneToMany(mappedBy = "cancellation")
    private List<Penalty> penalties;
}
