package com.vilgodskiy.modshare.user.api;

import com.vilgodskiy.modshare.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
@RequiredArgsConstructor
public class UserAssembler {

    public UserResponse assemble(User user) {
        return new UserResponse()
                .setId(user.getId())
                .setLogin(user.getLogin())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setMiddleName(user.getMiddleName())
                .setLocked(user.isLocked())
                .setPhone(user.getPhone())
                .setRole(user.getRole())
                .setTracingInfo(user.getTracingInfo());
    }
}