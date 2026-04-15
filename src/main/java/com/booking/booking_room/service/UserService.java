package com.booking.booking_room.service;

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
}
