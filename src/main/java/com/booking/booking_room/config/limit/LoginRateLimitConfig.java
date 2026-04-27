package com.booking.booking_room.config.limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

// LoginRateLimitConfig.java
@Configuration
public class LoginRateLimitConfig {

    // Chỉ cho 5 lần thử / 1 phút theo IP
    public static final Bandwidth LOGIN_BY_IP =
            Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));

    // Chỉ cho 3 lần thử / 5 phút theo email cụ thể
    // (chặn brute-force nhắm vào 1 tài khoản)
    public static final Bandwidth LOGIN_BY_EMAIL =
            Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(5)));
}
