package com.booking.booking_room.dto.auth;

import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.enumerate.user.UserRole;
import com.booking.booking_room.enumerate.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private UserRequest user;
    @JsonIgnore
    private String refreshToken;

    @Data
    @Builder
    public static class UserRequest {
        private Long id;
        private String email;
        private String phone;
        private UserRole role;
        private UserStatus status;
    }
}
