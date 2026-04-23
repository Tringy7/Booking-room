package com.booking.booking_room.repository;

import com.booking.booking_room.entity.otp.OTPInfo;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OTPInfoRepository extends JpaRepository<OTPInfo, Long> {
    Optional<OTPInfo> findByEmail(String email);

    Optional<OTPInfo> findByOtpAndEmail(String otp, String email);
}
