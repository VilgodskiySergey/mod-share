package com.vilgodskiy.modshare.util;

import com.elementsoft.common.Validator;

/**
 * @param <T> - Entity type
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Provides validate method to entity. Adds chain-effect
 */
public interface Validable<T> {

    default T validate(Validator<T> validator) {
        T entity = (T) this;
        validator.validate(entity).throwIfHasErrors();
        return entity;
    }
}
