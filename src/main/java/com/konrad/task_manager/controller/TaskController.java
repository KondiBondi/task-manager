package com.konrad.task_manager.controller;

import com.konrad.task_manager.model.criteria.TaskSearchCriteria;
import com.konrad.task_manager.model.customs.CustomResponse;
import com.konrad.task_manager.model.customs.CustomResponseImpl;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.model.dto.TaskStatusUpdateDto;
import com.konrad.task_manager.service.TaskSearchService;
import com.konrad.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final TaskSearchService taskSearchService;

    @PostMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<TaskDto>> getTasksByCriteria(Pageable pageable,
                                                            @RequestBody TaskSearchCriteria taskSearchCriteria) {
        log.info("Received request to find tasks with criteria: {}", taskSearchCriteria);
        Page<TaskDto> tasks = taskSearchService.find(pageable, taskSearchCriteria);
        log.info("Found {} tasks matching criteria", tasks.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        log.info("Received request to update task with ID: {}", id);
        TaskDto savedTask = taskService.update(id, taskDto);
        log.info("Successfully updated task with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponseImpl<>("", savedTask));
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse> createTask(@RequestBody TaskDto taskDto) {
        log.info("Received request to create a new task");
        TaskDto savedTask = taskService.create(taskDto);
        log.info("Successfully created task with ID: {}", savedTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomResponseImpl<>("", savedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Received request to delete task with ID: {}", id);
        if (taskService.delete(id)) {
            log.info("Successfully deleted task with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        log.warn("Task with ID: {} not found for deletion", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusUpdateDto statusUpdateDto) {
        log.info("Received request to update status of task with ID: {} to {}", id, statusUpdateDto.getStatus());
        TaskDto updatedTask = taskService.updateStatus(id, statusUpdateDto.getStatus());
        log.info("Successfully updated status of task with ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @PostMapping("/{taskId}/users/{userId}")
    public ResponseEntity<TaskDto> assignUser(@PathVariable Long taskId, @PathVariable Long userId) {
        log.info("Received request to assign user with ID: {} to task with ID: {}", userId, taskId);
        TaskDto taskDto = taskService.assignUser(taskId, userId);
        log.info("Successfully assigned user with ID: {} to task with ID: {}", userId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(taskDto);

    }
}
