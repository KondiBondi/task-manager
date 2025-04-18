package com.konrad.task_manager.repository;

import com.konrad.task_manager.model.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;


public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    boolean existsByTitleEqualsIgnoreCase(String title);

    @Query("SELECT t FROM task t JOIN t.assignedUsers u WHERE u.id = :userId")
    Set<TaskEntity> findAllTasksByAssignedUserId(@Param("userId") Long userId);
}
