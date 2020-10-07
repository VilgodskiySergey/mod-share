package com.vilgodskiy.modshare.mod.repository;

import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.mod.domain.Mod_;
import com.vilgodskiy.modshare.util.PredicateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vilgodskiy Sergey
 */
public class ModSpecificationFactory {

    public static Specification<Mod> filter(String searchString) {
        return (Root<Mod> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNoneBlank(searchString)) {
                predicates.add(PredicateUtil.likeIgnoreCase(cb, root, Mod_.TITLE, searchString));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
