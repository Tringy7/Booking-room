package com.booking.booking_room.service.auth;

import org.springframework.stereotype.Service;

import com.booking.booking_room.dto.auth.GoogleUserInfo;

@Service
public interface OauthVerifier {

    GoogleUserInfo verify(String idTokenString);
}
