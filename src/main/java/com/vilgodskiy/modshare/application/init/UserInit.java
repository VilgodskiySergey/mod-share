package com.vilgodskiy.modshare.application.init;

import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Users initialization
 */
@RequiredArgsConstructor
@Component
public class UserInit extends AbstractInit {

    private final UserService userService;

    @Override
    protected void init() {
        userService.create(null, null, null, "admin@mode-share.ru", null,
                "admin", "123456", Role.ADMINISTRATOR, null);
    }
}
