package com.konrad.task_manager.service.helpers;

import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.model.entity.TaskEntity;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.model.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskHelperService {

    public static TaskDto mapTaskEntityToTaskDto(TaskEntity taskEntity) {
        log.debug("Mapping TaskEntity to TaskDto: {}", taskEntity.getId());
        Set<UserDto> userDtoSet = new HashSet<>();
        if (taskEntity.getAssignedUsers() != null) {
            userDtoSet = taskEntity.getAssignedUsers().stream().map(
                    userEntity -> new UserDto(userEntity.getId(), userEntity.getName(), userEntity.getSurname(), userEntity.getEmail(), userEntity.getVersion())
            ).collect(Collectors.toSet());
        }
        TaskDto taskDto = TaskDto.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .status(taskEntity.getStatus())
                .deadline(taskEntity.getDeadline())
                .assignedUsers(userDtoSet)
                .version(taskEntity.getVersion())
                .build();
        log.debug("Successfully mapped TaskEntity to TaskDto: {}", taskDto.getId());
        return taskDto;
    }

    public static TaskEntity mapTaskDtoToTaskEntity(TaskDto taskDto) {
        log.debug("Mapping TaskDto to TaskEntity: {}", taskDto.getId());
        Set<UserEntity> userEntitySet = new HashSet<>();
        if (taskDto.getAssignedUsers() != null) {
            userEntitySet = taskDto.getAssignedUsers().stream().map(
                    userDto -> new UserEntity(userDto.getId(), userDto.getName(), userDto.getSurname(), userDto.getEmail(), userDto.getVersion())
            ).collect(Collectors.toSet());
        }

        TaskEntity taskEntity = TaskEntity.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .deadline(taskDto.getDeadline())
                .assignedUsers(userEntitySet)
                .version(taskDto.getVersion())
                .build();
        log.debug("Successfully mapped TaskDto to TaskEntity: {}", taskEntity.getId());
        return taskEntity;
    }

}
