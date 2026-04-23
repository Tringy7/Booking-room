package com.booking.booking_room.service.auth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.booking.booking_room.dto.auth.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.booking.booking_room.exception.VerificationException;

@Service
public class GoogleOAuthVerifier implements OauthVerifier {

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Override
    public GoogleUserInfo verify(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new JacksonFactory()
            )
                    .setAudience(Collections.singletonList(clientId)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new VerificationException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return GoogleUserInfo.builder()
                    .email(payload.getEmail())
                    .name((String) payload.get("name"))
                    .emailVerified(payload.getEmailVerified())
                    .build();

        } catch (Exception e) {
            throw new VerificationException("Google verification failed", e);
        }
    }
}
