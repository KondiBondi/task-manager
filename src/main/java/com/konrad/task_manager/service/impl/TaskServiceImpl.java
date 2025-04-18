package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.customs.TaskNotFoundException;
import com.konrad.task_manager.model.customs.UserNotFoundException;
import com.konrad.task_manager.model.entity.TaskEntity;
import com.konrad.task_manager.model.entity.UserEntity;
import com.konrad.task_manager.model.customs.ValidationException;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.repository.TaskRepository;
import com.konrad.task_manager.repository.UserRepository;
import com.konrad.task_manager.service.helpers.TaskHelperService;
import com.konrad.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskDto create(TaskDto taskDto) throws ValidationException{
        log.debug("Creating a new task with title: {}", taskDto.getTitle());
        validate(taskDto);
        TaskEntity newTaskEntity = TaskHelperService.mapTaskDtoToTaskEntity(taskDto);
        newTaskEntity.setVersion(1);

        TaskEntity taskEntity = this.taskRepository.saveAndFlush(newTaskEntity);
        log.debug("Successfully created task with ID: {}", taskEntity.getId());
        return TaskHelperService.mapTaskEntityToTaskDto(taskEntity);
    }

    private void validate(TaskDto taskDto) {
        log.debug("Validating Task with ID: {}", taskDto.getId());
        var title = taskDto.getTitle();
        if (StringUtils.isBlank(title)) throw new ValidationException("Title is empty");
        if (!taskRepository.existsByTitleEqualsIgnoreCase(title))
            throw new ValidationException("Title is not unique");
        log.debug("Validation passed for task title: {}", title);
    }

    @Override
    @Transactional
    public TaskDto update(Long id, TaskDto taskDto) {
        log.debug("Updating task with ID: {}", id);
        validate(taskDto);
        TaskEntity taskEntity = TaskHelperService.mapTaskDtoToTaskEntity(taskDto);

        TaskEntity savedTaskEntity = taskRepository.saveAndFlush(taskEntity);
        log.debug("Successfully updated task with ID: {}", savedTaskEntity.getId());
        return TaskHelperService.mapTaskEntityToTaskDto(savedTaskEntity);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        log.debug("Deleting task with ID: {}", id);
        return taskRepository.findById(id).map(task -> {
            task.getAssignedUsers().clear();
            taskRepository.saveAndFlush(task);
            taskRepository.deleteById(id);
            log.debug("Successfully deleted task with ID: {}", id);
            return true;
        }).orElseGet(() -> {
            log.warn("Task with ID: {} not found for deletion", id);
            return false;
        });
    }

    @Override
    @Transactional
    public TaskDto updateStatus(Long id, Status status) {
        log.debug("Updating status of task with ID: {} to {}", id, status);
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        taskEntity.setStatus(status);
        TaskEntity updatedTaskEntity = taskRepository.saveAndFlush(taskEntity);
        log.debug("Successfully updated status of task with ID: {}", id);
        return TaskHelperService.mapTaskEntityToTaskDto(updatedTaskEntity);
    }

    @Override
    @Transactional
    public TaskDto assignUser(Long taskId, Long userId) {
        log.debug("Assigning user with ID: {} to task with ID: {}", userId, taskId);
        TaskEntity taskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task " + taskId + " not found"));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));
        
        taskEntity.getAssignedUsers().add(userEntity);
        TaskEntity savedTaskEntity = taskRepository.saveAndFlush(taskEntity);
        log.debug("Successfully assigned user with ID: {} to task with ID: {}", userId, taskId);
        return TaskHelperService.mapTaskEntityToTaskDto(savedTaskEntity);
    }

}
