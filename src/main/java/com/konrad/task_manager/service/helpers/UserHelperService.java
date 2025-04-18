package com.konrad.task_manager.service.helpers;

import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.model.entity.UserEntity;
import com.konrad.task_manager.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class UserHelperService {

    public static boolean isUnique(String email, UserRepository userRepository) {
        log.debug("Checking if email is unique: {}", email);
        boolean isUnique = !userRepository.existsByEmailEqualsIgnoreCase(email);
        log.debug("Email uniqueness check result for {}: {}", email, isUnique);
        return isUnique;
    }

    public static UserEntity mapUserDtoToUserEntity(UserDto newUserDto) {
        log.debug("Mapping UserDto to UserEntity: {}", newUserDto);
        UserEntity userEntity =  UserEntity.builder()
                .name(newUserDto.getName())
                .surname(newUserDto.getSurname())
                .email(newUserDto.getEmail())
                .version(1)
                .build();
        log.debug("Successfully mapped UserDto to UserEntity: {}", userEntity);
        return userEntity;
    }

    public static UserDto mapUserEntityToUserDto(UserEntity userEntity) {
        log.debug("Mapping UserEntity to UserDto: {}", userEntity);
        UserDto userDto =  UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .email(userEntity.getEmail())
                .version(userEntity.getVersion())
                .build();
        log.debug("Successfully mapped UserEntity to UserDto: {}", userDto);
        return userDto;
    }

}
