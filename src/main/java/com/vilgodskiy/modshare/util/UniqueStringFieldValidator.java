package com.vilgodskiy.modshare.util;

import com.elementsoft.common.result.ValidationResult;
import com.elementsoft.common.result.ValidationResultImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

/**
 * @author Vilgodskiy Sergey
 */
public class UniqueStringFieldValidator {

    public UniqueStringFieldValidator(String entityName) {
        this.entityName = entityName;
    }

    public static final String IS_DUPLICATE = "'%s' с '%s' '%s' уже существует в системе";

    public final String entityName;

    public ValidationResult validate(String value, String fieldName, Supplier<Boolean> isDuplicate) {
        if (StringUtils.isNotBlank(value)) {
            if (isDuplicate.get()) {
                return new ValidationResultImpl(IS_DUPLICATE, entityName, fieldName, value);
            }
        }
        return ValidationResult.EMPTY;
    }

}
