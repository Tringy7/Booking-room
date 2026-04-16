package com.booking.booking_room.service.auth;

import com.booking.booking_room.dto.auth.RefreshTokenResponse;
import com.booking.booking_room.repository.UserRepository;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.booking_room.config.Common;
import com.booking.booking_room.dto.auth.LoginResponse;
import com.booking.booking_room.dto.auth.RegisterRequest;
import com.booking.booking_room.dto.auth.RegisterResponse;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.enumerate.user.UserRole;
import com.booking.booking_room.service.UserService;
import com.booking.booking_room.util.SecurityUtil;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse handleAuthentication(Authentication authentication) {
        User user = this.userService.getUserByEmail(authentication.getName());

        String accessToken = this.securityUtil.createAccessToken(user);
        String refreshToken = this.securityUtil.createRefreshToken(user);

        user.setRefreshToken(refreshToken);
        user = this.userService.updateUser(user);

        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserRequest  userRequest = LoginResponse.UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getUserStatus())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
        loginResponse.setUser(userRequest);
        loginResponse.setAccessToken(accessToken);

        return loginResponse;
    }

    public ResponseCookie getCookie(String refreshToken) {
        return ResponseCookie.from("refresh-Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.securityUtil.refreshTokenExpiration)
                .build();
    }

    public RegisterResponse handleRegister(RegisterRequest registerRequest) {
        if (this.userService.checkExistUser(registerRequest.getEmail())) {
            throw new EntityExistsException(Common.USER_EXISTS);
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .role(UserRole.USER)
                .build();
        user = this.userService.saveUser(user);

        return RegisterResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .build();
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return userService.getUserByRefreshTokenAndEmail(refreshToken, email);
    }

    public RefreshTokenResponse handleRefreshToken(User user) {
        String accessToken = this.securityUtil.createAccessToken(user);
        String refreshToken = this.securityUtil.createRefreshToken(user);

        user.setRefreshToken(refreshToken);
        this.userService.updateUser(user);
        return new RefreshTokenResponse(accessToken, refreshToken);
    }
}
