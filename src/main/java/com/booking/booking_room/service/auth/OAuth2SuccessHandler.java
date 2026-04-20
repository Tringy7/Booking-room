package com.booking.booking_room.service.auth;

import com.booking.booking_room.dto.auth.LoginResponse;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.service.UserService;
import com.booking.booking_room.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    // Generate jwt after login success
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        // Get user information when google has verified
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = this.userService.getUserByEmail(email);

        // Generate jwt
        LoginResponse loginResponse = this.authService.handleGoogleAuthentication(email);

        response.setHeader(HttpHeaders.SET_COOKIE, this.authService.getCookie(loginResponse.getRefreshToken()).toString());

        response.setStatus(HttpServletResponse.SC_OK);        // 200
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(loginResponse)    // Convert object → JSON
        );
    }
}