package com.vilgodskiy.modshare.application.config.security;


import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 29.07.2020
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    Optional<User> get(UUID id) {
        return userRepository.findById(id);
    }

    Optional<User> getByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }
}
