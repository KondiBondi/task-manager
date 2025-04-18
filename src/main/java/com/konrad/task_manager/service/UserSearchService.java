package com.konrad.task_manager.service;

import com.konrad.task_manager.model.criteria.UserSearchCriteria;
import com.konrad.task_manager.model.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSearchService {

    Page<UserDto> find(Pageable pageable, UserSearchCriteria userSearchCriteria);
}