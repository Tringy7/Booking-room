package com.booking.booking_room.config.limit;

import com.booking.booking_room.service.auth.RateLimiterService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Primary
@RequiredArgsConstructor
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Chỉ áp dụng cho endpoint login
        if (!request.getRequestURI().equals("/auth/login")
                || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy IP (xử lý cả trường hợp đứng sau Load Balancer)
        String ip = getClientIp(request);

        // Kiểm tra bucket theo IP trước (tầng bảo vệ thứ nhất)
        Bucket ipBucket = rateLimiterService.resolveBucketForLoginByIp(ip);
        if (!ipBucket.tryConsume(1)) {
            sendTooManyRequestsResponse(response, "Quá nhiều yêu cầu từ IP này.");
            return;
        }

        // Cho request đi qua
        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim(); // lấy IP đầu tiên
        }
        return request.getRemoteAddr();
    }

    private void sendTooManyRequestsResponse(HttpServletResponse response,
                                             String message) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"error\": \"" + message + "\", \"retryAfter\": 60}"
        );
    }
}