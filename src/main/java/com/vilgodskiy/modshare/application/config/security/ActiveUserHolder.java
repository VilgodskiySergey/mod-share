package com.vilgodskiy.modshare.application.config.security;

import com.vilgodskiy.modshare.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = WebApplicationContext.SCOPE_REQUEST)
@RequiredArgsConstructor
public class ActiveUserHolder {

    private static final ThreadLocal<User> activeUser = new ThreadLocal<>();

    public static Optional<User> getActiveUserOpt() {
        return Optional.ofNullable(activeUser.get());
    }

    public static User getActiveUser() {
        return Optional.ofNullable(activeUser.get()).orElseThrow();
    }

    public static void setActiveUser(User activeUser) {
        ActiveUserHolder.activeUser.set(activeUser);
    }
}
