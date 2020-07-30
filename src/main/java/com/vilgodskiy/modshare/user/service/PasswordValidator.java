package com.vilgodskiy.modshare.user.service;

import com.elementsoft.common.ValidatorBuilder;
import com.elementsoft.common.result.ValidationResult;
import com.vilgodskiy.modshare.user.domain.User;
import org.springframework.stereotype.Component;

/**
 * @author Vilgodskiy Sergey 24.07.2020
 *
 * Password validator
 */
@Component
public class PasswordValidator {

    public ValidationResult validate(String password) {
        return new ValidatorBuilder().validateEmptyAndSize(
                password, User.PASSWORD_MIN_LENGTH, User.PASSWORD_MAX_LENGTH, "Пароль").get();
    }
}
