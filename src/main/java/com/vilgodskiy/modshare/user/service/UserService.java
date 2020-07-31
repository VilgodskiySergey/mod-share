package com.vilgodskiy.modshare.user.service;

import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.domain.User_;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Classic service lair class for user buissiness loogic
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;
    private final UserUniqueStringFieldValidator userUniqueStringFieldValidator;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create user
     */
    public User create(String firstName, String lastName, String middleName, String email, String phone,
                       String login, String password, Role role, User executor) {
        passwordValidator.validate(password).throwIfHasErrors();
        userUniqueStringFieldValidator.validate(email, User_.EMAIL,
                () -> userRepository.existsByEmailIgnoreCase(email)).throwIfHasErrors();
        userUniqueStringFieldValidator.validate(login, "Логин",
                () -> userRepository.existsByLoginIgnoreCase(login)).throwIfHasErrors();
        return new User(firstName, lastName, middleName, email, phone,
                login, passwordEncoder.encode(password), role, executor)
                .saveTo(userRepository);
    }

    /**
     * Update user
     */
    public User update(UUID id, String firstName, String lastName, String middleName, String phone, User executor) {
        return userRepository.getOrThrow(id)
                .update(firstName, lastName, middleName, phone, executor)
                .saveTo(userRepository);
    }

    /**
     * Delete user
     *
     * @param id - user's ID
     */
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
