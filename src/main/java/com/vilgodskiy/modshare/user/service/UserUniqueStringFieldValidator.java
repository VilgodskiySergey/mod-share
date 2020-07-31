package com.vilgodskiy.modshare.user.service;

import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author Stetskevich Roman
 */
@Component
@AllArgsConstructor
public class UserUniqueStringFieldValidator {

    public final static String IS_DUPLICATE = "Профиль с '%s' '%s' уже существует в системе";

    public ValidationResult validate(String value, String fieldName, Supplier<Boolean> isDuplicate) {
        if (StringUtils.isNotBlank(value)) {
            if (isDuplicate.get()) {
                return new ValidationResultImpl(IS_DUPLICATE, fieldName, value);
            }
        }
        return ValidationResult.EMPTY;
    }
}
