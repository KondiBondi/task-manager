package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.customs.ValidationException;
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

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_whenEmailIsNotUnique_shouldThrowValidationException() {
        UserDto newUserDto = UserDto.builder().email("glen.quagmire@family.guy").build();
        when(userRepository.existsByEmailEqualsIgnoreCase("glen.quagmire@family.guy")).thenReturn(true);
        assertThatThrownBy(
                () -> userService.create(newUserDto)
        ).isInstanceOf(ValidationException.class).hasMessage("User email is not unique");
    }

    @Test
    void create_whenUserIsValid_shouldCreateUserEntityAndReturnUserDto() {
        UserDto newUserDto = UserDto.builder().name("Glen").surname("Quagmire").email("glen.quagmire@family.guy").build();
        UserEntity userEntity = UserEntity.builder().name("Glen").surname("Quagmire").email("glen.quagmire@family.guy").build();
        UserEntity savedUserEntity = UserEntity.builder().id(1L).name("Glen").surname("Quagmire").email("glen.quagmire@family.guy").build();
        when(userRepository.existsByEmailEqualsIgnoreCase("glen.quagmire@family.guy")).thenReturn(false);
        when(userRepository.saveAndFlush(userEntity)).thenReturn(savedUserEntity);
        var result = userService.create(newUserDto);
        assertThat(result).isInstanceOf(UserDto.class);
        assertThat(result.getEmail()).isEqualTo("glen.quagmire@family.guy");
    }

    @Test
    void delete_whenUserExistAndIsNotAssignedToAnyTasks_shouldReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findAllTasksByAssignedUserId(1L)).thenReturn(Set.of());
        assertThat(userService.delete(1L)).isTrue();
    }

    @Test
    void delete_whenUserExistAndIsToTask_shouldThrowValidationException() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findAllTasksByAssignedUserId(1L)).thenReturn(Set.of(new TaskEntity()));
        assertThatThrownBy(
                () -> userService.delete(1L)
        ).isInstanceOf(ValidationException.class).hasMessage("User assigned to a task cannot be removed");
    }

    @Test
    void delete_whenUserDoesNotExist_shouldReturnFalse() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThat(userService.delete(1L)).isFalse();
    }

}