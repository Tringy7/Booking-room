package com.booking.booking_room.service;

import com.booking.booking_room.enumerate.user.Provider;
import com.booking.booking_room.enumerate.user.UserRole;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import com.booking.booking_room.config.Common;
import com.booking.booking_room.entity.user.User;
import com.booking.booking_room.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean checkExistUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(Common.USER_NOT_FOUND));
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

    public void findOrCreateUser(String email) {
        if (checkExistUser(email)) {
            throw new EntityExistsException(Common.USER_EXISTS);
        }
        else {
            User user = User.builder().email(email).provider(Provider.GOOGLE).role(UserRole.USER).build();
            userRepository.save(user);
        }
    }
}
