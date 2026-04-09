package com.booking.booking_room.entity.booking;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.information.Information;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "cancellation_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationPolicy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "information_id", nullable = false)
    private Information information;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer cancellationDeadlineInDays;

    @Column(nullable = false)
    private Integer refundPercentage;

    @Column(nullable = true)
    private Long minPenaltyAmount;

    @Column(nullable = true)
    private Long maxPenaltyAmount;
}
