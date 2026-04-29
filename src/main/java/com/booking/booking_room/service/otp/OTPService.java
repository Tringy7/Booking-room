package com.booking.booking_room.service.otp;

import com.booking.booking_room.entity.otp.OTPInfo;
import com.booking.booking_room.exception.VerificationException;
import com.booking.booking_room.repository.OTPInfoRepository;
import com.booking.booking_room.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final OTPInfoRepository otpInfoRepository;
    private final EmailService emailService;
    private final UserService userService;

    // generate token for send email
    public String generateOTP(String email) {
        // Generate OTP
        int otpValue = 100_000 + new Random().nextInt(900_000);
        String otp = String.valueOf(otpValue);

        // 2. Lấy record nếu tồn tại
        OTPInfo otpInfo = otpInfoRepository.findByEmail(email)
                .orElse(new OTPInfo());

        otpInfo.setEmail(email);
        otpInfo.setOtp(otp);

        // 3. Thời gian
        otpInfo.setGenerateAt(LocalDateTime.now());
        otpInfo.setExpiredAt(LocalDateTime.now().plusMinutes(5));

        otpInfoRepository.save(otpInfo);
        return otp;
    }

    @Async
    public CompletableFuture<Boolean> sendOTPByEmail(String email) {
        String otp = generateOTP(email);
        // Compose the email content
        String subject = "OTP Verification";
        CompletableFuture<Void> emailSendingFuture = this.emailService.sendEmailFromTemplateSync(email, otp, subject);

        return emailSendingFuture.thenApplyAsync(result -> true)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }

    public void checkCodeOTP(String email, String code) {
        OTPInfo otpInfo = otpInfoRepository.findByOtpAndEmail(code, email)
                .orElseThrow(() -> new VerificationException("OTP not found"));

        // 1. Check OTP match
        if (!otpInfo.getOtp().equals(code)) {
            throw new VerificationException("Invalid OTP");
        }

        // 2. Check expired
        if (otpInfo.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new VerificationException("OTP expired");
        }

        this.userService.verifyEmail(email);
    }
}
