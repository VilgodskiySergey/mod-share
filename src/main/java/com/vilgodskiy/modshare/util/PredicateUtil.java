package com.vilgodskiy.modshare.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
public class PredicateUtil {

    /**
     * Get predicate String case insensitive comparison "Starts with"
     */
    public static <T> Predicate startWithIgnoreCase(CriteriaBuilder cb, Root<T> root, String field, String searchString) {
        return cb.like(cb.upper(root.get(field)),
                SqlUtil.replacePercentSignForDbSearch(searchString.toUpperCase()) + "%");
    }

    /**
     * Get predicate String case insensitive comparison
     */
    public static <T> Predicate likeIgnoreCase(CriteriaBuilder cb, Root<T> root, String field, String searchString) {
        return cb.like(cb.upper(root.get(field)),
                "%" + SqlUtil.replacePercentSignForDbSearch(searchString.toUpperCase()) + "%");
    }

    /**
     * Get predicate String case insensitive comparison - with Join-operation
     */
    public static <T> Predicate likeIgnoreCase(CriteriaBuilder cb, Join<Object, Object> root, String field, String searchString) {
        return cb.like(cb.upper(root.get(field)),
                "%" + SqlUtil.replacePercentSignForDbSearch(searchString.toUpperCase()) + "%");
    }
}