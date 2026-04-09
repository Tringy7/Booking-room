package com.booking.booking_room.entity.information;

import java.util.List;

import com.booking.booking_room.entity.BaseEntity;
import com.booking.booking_room.entity.booking.CancellationPolicy;
import com.booking.booking_room.entity.booking.Review;
import com.booking.booking_room.entity.room.Room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "informations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Information extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(nullable = true)
    private String address;
    private Float latitude;
    private Float longitude;
    private Integer starRating;
    private Long totalRoom;

    @OneToOne(mappedBy = "information")
    private Policy policy;

    @OneToOne(mappedBy = "information")
    private Contact contact;

    @OneToMany(mappedBy = "information")
    private List<Room> rooms;

    @OneToMany(mappedBy = "information")
    private List<CancellationPolicy> cancellationPolicies;

    @OneToMany(mappedBy = "information")
    private List<Review> reviews;
}
