package com.konrad.task_manager.service;

import com.konrad.task_manager.model.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);
    boolean delete(Long id);

}
