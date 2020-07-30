package com.vilgodskiy.modshare.user.service;

import com.elementsoft.common.Validator;
import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Stetskevich Roman
 */
@Component
@AllArgsConstructor
public class EmailUniqueValidator implements Validator<User> {

    private final static String EMAIL_DUPLICATE = "Профиль с email '%s' уже существует в системе";

    private final UserRepository userRepository;


    @Override
    public ValidationResult validate(User user) {
        if (StringUtils.isNotBlank(user.getEmail())) {
            if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
                return new ValidationResultImpl(EMAIL_DUPLICATE, user.getEmail());
            }
        }
        return ValidationResult.EMPTY;
    }
}
