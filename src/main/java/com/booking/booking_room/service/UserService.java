package com.booking.booking_room.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.booking.booking_room.config.system.Common;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(Common.USER_NOT_FOUND));
    }

    public User getUserByEmailOauth(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent() ? user.get() : null;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return userRepository.findByRefreshTokenAndEmail(refreshToken, email)
                .orElseThrow(() -> new EntityNotFoundException(Common.USER_NOT_FOUND));
    }

    public void verifyEmail(String email) {
        User user = getUserByEmail(email);
        user.setEmailVerified(true);
        userRepository.save(user);
    }
}
