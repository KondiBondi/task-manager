package com.konrad.task_manager.service;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.dto.TaskDto;

public interface TaskService {
    TaskDto create(TaskDto task);

    TaskDto update(Long id, TaskDto taskDto);

    boolean delete(Long id);

    TaskDto updateStatus(Long id, Status status);

    TaskDto assignUser(Long taskId, Long userId);
}
