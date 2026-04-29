package com.booking.booking_room.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.booking_room.entity.information.Information;

public interface InformationRepository extends JpaRepository<Information, Long> {

    Optional<Information> findFirstByIsDeletedFalseOrderByIdAsc();
}
