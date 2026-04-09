package com.booking.booking_room.entity.information;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.booking.booking_room.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime checkInFrom;
    @Column(nullable = false)
    private LocalDateTime checkInTo;
    @Column(nullable = false)
    private LocalTime checkOutBefore;
    @Column(nullable = false)
    private int minAgeCheckin;
    private String petPolicy;
    private String smokingPolicy;

    @OneToOne
    @JoinColumn(name = "information_id")
    private Information information;
}
