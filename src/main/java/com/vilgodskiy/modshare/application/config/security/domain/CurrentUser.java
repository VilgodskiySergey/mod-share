package com.vilgodskiy.modshare.application.config.security.domain;

import com.vilgodskiy.modshare.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by Vilgodskiy Sergey 29.07.2020
 */
@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public CurrentUser(User user) {
        super(user.getLogin(), user.getPasswordHash(), true, true,
                true, !user.isLocked(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}
