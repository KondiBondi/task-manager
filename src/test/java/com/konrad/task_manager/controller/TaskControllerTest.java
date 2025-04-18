package com.konrad.task_manager.controller;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.criteria.TaskSearchCriteria;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.model.dto.TaskStatusUpdateDto;
import com.konrad.task_manager.service.TaskSearchService;
import com.konrad.task_manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;
    @Mock
    private TaskSearchService taskSearchService;
    @InjectMocks
    private TaskController taskController;

    @Test
    void getTasksByCriteria_shouldReturnOk() {
        List<TaskDto> taskDtoList = List.of(new TaskDto(), new TaskDto());
        Page<TaskDto> taskDtoPage = new PageImpl<>(taskDtoList);
        when(taskSearchService.find(any(Pageable.class), any(TaskSearchCriteria.class))).thenReturn(taskDtoPage);
        var result = taskController.getTasksByCriteria(Pageable.unpaged(), new TaskSearchCriteria());
        assertThat(result).satisfies(
                task -> {
                    assertThat(result).isInstanceOf(ResponseEntity.class);
                    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
                }
        );
    }

    @Test
    void updateTask_shouldReturnOk() {
        when(taskService.update(anyLong(), any(TaskDto.class))).thenReturn(new TaskDto());
        var result = taskController.updateTask(1L, new TaskDto());
        assertThat(result).isInstanceOf(ResponseEntity.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createTask_shouldReturnCreated() {
        when(taskService.create(any(TaskDto.class))).thenReturn(new TaskDto());
        var result = taskController.createTask(new TaskDto());
        assertThat(result).isInstanceOf(ResponseEntity.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteTask_shouldReturnNoContent() {
        when(taskService.delete(anyLong())).thenReturn(true);
        var response = taskController.deleteTask(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteTask_shouldReturnNotFound() {
        when(taskService.delete(anyLong())).thenReturn(false);
        var response = taskController.deleteTask(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateTaskStatus_shouldReturnOk() {
        when(taskService.updateStatus(anyLong(), any(Status.class))).thenReturn(new TaskDto());
        var response = taskController.updateTaskStatus(1L, TaskStatusUpdateDto.builder().status(Status.ON_HOLD).build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void assignUser_shouldReturnOk() {
        when(taskService.assignUser(anyLong(), anyLong())).thenReturn(new TaskDto());
        var response = taskController.assignUser(1L, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



}