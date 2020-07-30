package com.vilgodskiy.modshare.util;

import com.vilgodskiy.modshare.application.exception.NotFoundObjectException;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * @param <T>  - Entity type
 * @param <ID> - ID type
 * @author Vilgodskiy Sergey 24.07.2020
 * <p>
 * Provides "Get Or Throw" methods for JPA-repository
 */
public interface GetOrThrowRepository<T, ID> {

    String NOT_FOUND = "Объект не найден";

    default T getOrThrow(ID id) {
        if (id == null) {
            throw new NotFoundObjectException(getNotFoundMessage());
        }
        return findById(id).orElseThrow(() -> new NotFoundObjectException(NOT_FOUND));
    }

    default T getOrThrow(Specification<T> specification) {
        return findOne(specification)
                .orElseThrow(() -> new NotFoundObjectException(NOT_FOUND));
    }

    default String getNotFoundMessage() {
        return NOT_FOUND;
    }

    Optional<T> findById(ID id);

    Optional<T> findOne(Specification<T> specification);

}
