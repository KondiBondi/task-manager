package com.konrad.task_manager.controller;

import com.konrad.task_manager.model.criteria.UserSearchCriteria;
import com.konrad.task_manager.model.customs.CustomResponse;
import com.konrad.task_manager.model.customs.CustomResponseImpl;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.service.UserSearchService;
import com.konrad.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserSearchService userSearchService;

    @PostMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDto>> getUsersByCriteria(Pageable pageable,
                                                            @RequestBody UserSearchCriteria userSearchCriteria) {
        log.info("Received request to find users with criteria: {}", userSearchCriteria);
        Page<UserDto> users = userSearchService.find(pageable, userSearchCriteria);
        log.info("Found {} users matching criteria", users.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse> createUser(@RequestBody UserDto newUserDto) {
        log.info("Received request to create a new user");
        UserDto savedUser = userService.create(newUserDto);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomResponseImpl<>("", savedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        if (userService.delete(id)) {
            log.info("Successfully deleted user with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        log.warn("User with ID: {} not found for deletion", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
