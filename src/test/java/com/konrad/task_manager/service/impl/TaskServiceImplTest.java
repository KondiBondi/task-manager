package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.customs.TaskNotFoundException;
import com.konrad.task_manager.model.customs.UserNotFoundException;
import com.konrad.task_manager.model.customs.ValidationException;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.model.entity.TaskEntity;
import com.konrad.task_manager.model.entity.UserEntity;
import com.konrad.task_manager.repository.TaskRepository;
import com.konrad.task_manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void create_whenTitleIsEmpty_shouldThrowValidationException() {
        var noTitleNewTaskDto = TaskDto.builder().build();

        assertThatThrownBy(() ->
                taskService.create(noTitleNewTaskDto)
        ).isInstanceOf(ValidationException.class).hasMessage("Title is empty");
    }

    @Test
    void create_whenTitleIsNotUnique_shouldThrowValidationException() {
        var newTaskDto = TaskDto.builder().title("Take over the world").build();
        assertThatThrownBy(() ->
                taskService.create(newTaskDto)
        ).isInstanceOf(ValidationException.class).hasMessage("Title is not unique");
    }

    @Test
    void create_whenNewTaskDtoIsValid_shouldCreateTaskEntityAndReturnTaskDto() {
        Set<UserDto> newUserDtoSet = new HashSet<>();
        var newTaskDto = TaskDto.builder().title("Take over the world").assignedUsers(newUserDtoSet).build();
        var savedTaskEntity = TaskEntity.builder().id(1L).title("Take over the world").assignedUsers(new HashSet<>()).version(1).build();

        when(taskRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(true);
        when(taskRepository.saveAndFlush(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        var result = taskService.create(newTaskDto);

        assertThat(result).satisfies(task -> {
            assertThat(task.getTitle()).isEqualTo("Take over the world");
            assertThat(task.getId()).isEqualTo(1L);
            assertThat(task.getVersion()).isEqualTo(1);
        });
    }

    @Test
    void update_whenTitleIsEmpty_shouldThrowValidationException() {
        var noTitleTaskDto = TaskDto.builder().build();

        assertThatThrownBy(() ->
                taskService.update(1L, noTitleTaskDto)
        ).isInstanceOf(ValidationException.class).hasMessage("Title is empty");
    }

    @Test
    void update_whenTitleIsNotUnique_shouldThrowValidationException() {
        var newTaskDto = TaskDto.builder().title("Take over the world").build();
        assertThatThrownBy(() ->
                taskService.update(1L, newTaskDto)
        ).isInstanceOf(ValidationException.class).hasMessage("Title is not unique");
    }

    @Test
    void update_whenNewTaskDtoIsValid_shouldUpdateTaskEntityAndReturnTaskDto() {
        Set<UserDto> userDtoSet = new HashSet<>();
        var taskDto = TaskDto.builder().title("Take over the world").assignedUsers(userDtoSet).build();
        var savedTaskEntity = TaskEntity.builder().id(1L).title("Take over the world").assignedUsers(new HashSet<>()).version(1).build();

        when(taskRepository.existsByTitleEqualsIgnoreCase(anyString())).thenReturn(true);
        when(taskRepository.saveAndFlush(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        var result = taskService.update(1L, taskDto);

        assertThat(result).satisfies(task -> {
            assertThat(task.getTitle()).isEqualTo("Take over the world");
            assertThat(task.getId()).isEqualTo(1L);
            assertThat(task.getVersion()).isEqualTo(1);
        });
    }

    @Test
    void delete_whenTaskExists_shouldReturnTrue() {
        UserEntity userEntity = UserEntity.builder().id(1L).build();
        Set<UserEntity> userEntitySet = new HashSet<>();
        userEntitySet.add(userEntity);
        TaskEntity taskEntity = TaskEntity.builder().id(1L).assignedUsers(userEntitySet).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertThat(taskService.delete(1L)).isTrue();
    }

    @Test
    void delete_whenTaskDoesNotExist_shouldReturnFalse() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(taskService.delete(1L)).isFalse();
    }

    @Test
    void updateStatus_whenTaskNotFound_shouldThrowTaskNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> taskService.updateStatus(1L, Status.DONE)
        ).isInstanceOf(TaskNotFoundException.class).hasMessage("Task with ID 1 not found");
    }

    @Test
    void updateStatus_whenTaskExist_shouldUpdateStatus() {
        TaskEntity taskEntity = TaskEntity.builder().id(1L).build();
        TaskEntity taskEntity1 = TaskEntity.builder().id(1L).status(Status.DONE).build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.saveAndFlush(taskEntity1)).thenReturn(taskEntity1);

        assertThat(taskService.updateStatus(1L, Status.DONE).getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    void assignUser_whenTaskDoesNotExist_shouldReturnTaskNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> taskService.assignUser(1L, 1L)
        ).isInstanceOf(TaskNotFoundException.class).hasMessage("Task 1 not found");
    }

    @Test
    void assignUser_whenTaskDoesNotExist_shouldReturnUserNotFoundException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(TaskEntity.builder().build()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> taskService.assignUser(1L, 1L)
        ).isInstanceOf(UserNotFoundException.class).hasMessage("User 1 not found");
    }

    @Test
    void assignUser_whenTaskAndUserExist_shouldReturnTaskDtoWithAssignedUser() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .name("Peter")
                .surname("Griffin")
                .email("petergriffin@family.guy")
                .version(1)
                .build();
        TaskEntity taskEntity = TaskEntity.builder()
                .id(1L)
                .title("Pawtucket Brewery Team Meeting")
                .description("Meeting in main hall. Pawtuket Patriot Ale as main dish")
                .deadline(LocalDate.of(2026, 6, 6))
                .assignedUsers(new HashSet<>())
                .status(Status.ON_HOLD)
                .version(2)
                .build();

        TaskEntity savedTaskEntity = TaskEntity.builder()
                .id(1L)
                .title("Pawtucket Brewery Team Meeting")
                .description("Meeting in main hall. Pawtuket Patriot Ale as main dish")
                .deadline(LocalDate.of(2026, 6, 6))
                .assignedUsers(Set.of(userEntity))
                .status(Status.ON_HOLD)
                .version(2)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(taskRepository.saveAndFlush(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        var response = taskService.assignUser(1L, 1L);


        assertThat(response.getAssignedUsers()).isNotNull();
        assertThat(response).isInstanceOf(TaskDto.class);
        assertThat(response.getAssignedUsers()).isInstanceOf(Set.class);
        assertThat(response.getAssignedUsers().size()).isEqualTo(1);


    }


}