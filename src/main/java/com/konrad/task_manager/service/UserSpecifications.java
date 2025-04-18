package com.konrad.task_manager.service;

import com.konrad.task_manager.model.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class UserSpecifications {
    public static Specification<UserEntity> fullame(String fullName) {
        return (root, query, builder) -> {
            Expression<String> fullNameExpression = builder.concat(
                    builder.concat(root.get("name"), " "), root.get("surname"));
            Predicate fullNamePredicate = builder.like(fullNameExpression, "%" + fullName + "%");
            return builder.and(fullNamePredicate);
        };
    }

    public static Specification<UserEntity> email(String email) {
        return (root, query, builder) -> builder.like(root.get("email"), "%" + email + "%");
    }
}
