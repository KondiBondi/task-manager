package com.konrad.task_manager.service;

import com.konrad.task_manager.model.criteria.TaskSearchCriteria;
import com.konrad.task_manager.model.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskSearchService {

    Page<TaskDto> find(Pageable pageable, TaskSearchCriteria taskSearchCriteria);
}
