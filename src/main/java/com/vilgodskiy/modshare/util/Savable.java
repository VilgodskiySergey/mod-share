package com.vilgodskiy.modshare.util;

import org.springframework.data.repository.CrudRepository;

/**
 * @param <T> - Entity type
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Provides save method to entity. Adds chain-effect
 */
public interface Savable<T> {

    default T saveTo(CrudRepository<T, ?> repository) {
        return repository.save((T) this);
    }

}
