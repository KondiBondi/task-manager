package com.konrad.task_manager.controller;

import com.konrad.task_manager.model.criteria.UserSearchCriteria;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.service.UserSearchService;
import com.konrad.task_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserSearchService userSearchService;
    @InjectMocks
    private UserController userController;

    @Test
    void getUsersByCriteria_shouldReturnOk() {
        when(userSearchService.find(any(Pageable.class), any(UserSearchCriteria.class)))
                .thenReturn(new PageImpl<>(List.of(new UserDto())));
        var response = userController.getUsersByCriteria(Pageable.unpaged(), new UserSearchCriteria());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createUser_shouldReturnCreated() {
        when(userService.create(any(UserDto.class))).thenReturn(new UserDto());
        var response = userController.createUser(new UserDto());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteUser_shouldReturnNoContent() {
        when(userService.delete(anyLong())).thenReturn(true);
        var response = userController.deleteUser(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void deleteUser_shouldReturnNotFound() {
        when(userService.delete(anyLong())).thenReturn(false);
        var response = userController.deleteUser(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}