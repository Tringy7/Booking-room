package com.booking.booking_room.service.auth;

import com.booking.booking_room.entity.user.UserProfile;
import com.booking.booking_room.enumerate.user.Provider;
import com.booking.booking_room.enumerate.user.UserStatus;
import com.booking.booking_room.service.UserProfileService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.booking_room.config.Common;
import com.booking.booking_room.dto.auth.GoogleUserInfo;
import com.booking.booking_room.dto.auth.LoginResponse;
import com.booking.booking_room.dto.auth.RefreshTokenResponse;
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
    private final OauthVerifier oauthVerifier;
    private final UserProfileService userProfileService;

    public LoginResponse handleAuthentication(Authentication authentication) {
        User user = this.userService.getUserByEmail(authentication.getName());

        String accessToken = this.securityUtil.createAccessToken(user);
        String refreshToken = this.securityUtil.createRefreshToken(user);

        user.setRefreshToken(refreshToken);
        user = this.userService.updateUser(user);

        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserRequest userRequest = LoginResponse.UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getUserProfile() != null ? user.getUserProfile().getFullName() : null)
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
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .provider(Provider.LOCAL)
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

    public LoginResponse handleGoogleLogin(String idTokenString) throws Exception {
        // handle token response by google
        GoogleUserInfo info = oauthVerifier.verify(idTokenString);

        // save user current login by google
        User user = this.userService.getUserByEmail(info.getEmail());
        if (user == null) {
            user = User.builder()
                    .email(info.getEmail())
                    .password(passwordEncoder.encode(Common.OAUTH_GOOGLE_PASSWORD))
                    .role(UserRole.USER)
                    .isEmailVerified(true)
                    .isPhoneVerified(false)
                    .provider(Provider.GOOGLE)
                    .build();
        }
        if (user.getUserProfile() == null) {
            UserProfile profile = UserProfile.builder().fullName(info.getName()).build();
            this.userProfileService.save(profile);
            user.setUserProfile(profile);
        }

        // create jwt
        String accessToken = this.securityUtil.createAccessToken(user);
        String refreshToken = this.securityUtil.createRefreshToken(user);

        user.setRefreshToken(refreshToken);
        user = this.userService.updateUser(user);

        LoginResponse loginResponse = new LoginResponse();
        LoginResponse.UserRequest userRequest = LoginResponse.UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getUserProfile() != null ? user.getUserProfile().getFullName() : null)
                .status(user.getUserStatus())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
        loginResponse.setUser(userRequest);
        loginResponse.setAccessToken(accessToken);

        return loginResponse;
    }

}