package com.konrad.task_manager.service;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.entity.TaskEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskSpecifications {

    public static Specification<TaskEntity> text(String wantedExpression, String columnName) {
        return (root, query, builder) -> builder.like(root.get(columnName), "%" + wantedExpression + "%");
    }

    public static Specification<TaskEntity> deadline(LocalDate deadlineFrom, LocalDate deadlineTo, String columnName) {
        return (root, query, builder) -> builder.between(root.get(columnName), deadlineFrom, deadlineTo);
    }

    public static Specification<TaskEntity> statusText(Status status, String columnName) {
        return (root, query, builder) -> builder.equal(root.get(columnName), status);
    }
}
