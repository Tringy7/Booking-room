package com.booking.booking_room.controller.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.booking.booking_room.dto.auth.*;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.exception.CommonException;
import com.booking.booking_room.service.otp.OTPService;
import com.booking.booking_room.util.SecurityUtil;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.booking.booking_room.annotation.ApiMessage;
import com.booking.booking_room.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;
    private final OTPService otpService;
    private final SecurityUtil securityUtil;

    @PostMapping("/register")
    @ApiMessage("Register successful")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = this.authService.handleRegister(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    @ApiMessage("Login successful")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse loginRes = this.authService.handleAuthentication(authentication);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, this.authService.getCookie(loginRes.getRefreshToken()).toString())
                .body(loginRes);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@CookieValue(name = "refresh-Token", defaultValue = "hehe") String refreshToken) throws Exception {
        if (refreshToken.equals("hehe")) {
            throw new CommonException("Not refreshed token");
        }
        Jwt decodeToken = this.securityUtil.verfifyRefreshToken(refreshToken);
        String email = decodeToken.getSubject();

        User currentUser = this.authService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new CommonException("Invalid refresh token");
        }

        RefreshTokenResponse refreshTokenResponse = this.authService.handleRefreshToken(currentUser);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, this.authService.getCookie(refreshTokenResponse.getRefreshToken()).toString())
                .body(refreshTokenResponse);
    }

    // Login by google
    @PostMapping("/google")
    @ApiMessage("Login with Google successfully")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("token");
        try {
            LoginResponse response = this.authService.handleGoogleLogin(idTokenString);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,
                            this.authService.getCookie(response.getRefreshToken()).toString())
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Send otp by gmail
    @PostMapping("/token")
    @ApiMessage("Send token for email user")
    public ResponseEntity<Map<String, Object>> createEmailVerifyToken(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        CompletableFuture<Boolean> res =  this.otpService.sendOTPByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP is being sent");

        return ResponseEntity.ok(response);
    }

    // Confirm otp
    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmCodeFromGmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");

        this.otpService.checkCodeOTP(email, code);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @ApiMessage("Log out")
    public ResponseEntity<Void> logout() {
        if (!this.securityUtil.getCurrentUserLogin().isPresent()) {
            throw new EntityNotFoundException("Not exist email");

        }
        String email = this.securityUtil.getCurrentUserLogin().get();
        this.authService.handleLogout(email);

        return  ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, this.authService.deleteCookie().toString())
                .build();
    }
}
