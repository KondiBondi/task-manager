package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.customs.ValidationException;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.model.entity.TaskEntity;
import com.konrad.task_manager.model.entity.UserEntity;
import com.konrad.task_manager.repository.TaskRepository;
import com.konrad.task_manager.repository.UserRepository;
import com.konrad.task_manager.service.helpers.UserHelperService;
import com.konrad.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public UserDto create(UserDto newUserDto) {
        log.debug("Received request to create a new user with email: {}", newUserDto.getEmail());
        if (!UserHelperService.isUnique(newUserDto.getEmail(), userRepository)) {
            throw new ValidationException("User email is not unique");
        }
        UserEntity newUserEntity = UserHelperService.mapUserDtoToUserEntity(newUserDto);
        UserEntity savedUserEntity = userRepository.saveAndFlush(newUserEntity);
        log.info("Successfully created user with ID: {}", savedUserEntity.getId());
        return UserHelperService.mapUserEntityToUserDto(savedUserEntity);
    }

    @Override
    public boolean delete(Long id) {
        log.info("Received request to delete user with ID: {}", id);
        if (userRepository.existsById(id)) {
            Set<TaskEntity> taskEntities = taskRepository.findAllTasksByAssignedUserId(id);
            if (taskEntities.isEmpty()) {
                userRepository.deleteById(id);
                log.info("Successfully deleted user with ID: {}", id);
                return true;
            }
            log.warn("Validation failed: User with ID: {} is assigned to tasks and cannot be deleted", id);
            throw new ValidationException("User assigned to a task cannot be removed");
        }
        log.warn("User with ID: {} not found for deletion", id);
        return false;
    }
}
