package com.vilgodskiy.modshare.user.repository;

import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.domain.User_;
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
public class UserSpecificationFactory {

    public static Specification<User> filter(Role role, String searchString) {
        return (Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (role != null) {
                predicates.add(cb.equal(root.get(User_.ROLE), role));
            }
            if (StringUtils.isNoneBlank(searchString)) {
                predicates.add(cb.or(
                        PredicateUtil.likeIgnoreCase(cb, root, User_.LOGIN, searchString),
                        PredicateUtil.likeIgnoreCase(cb, root, User_.EMAIL, searchString)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
