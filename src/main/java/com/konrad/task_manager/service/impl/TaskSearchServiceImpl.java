package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.Status;
import com.konrad.task_manager.model.criteria.TaskSearchCriteria;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.model.entity.TaskEntity;
import com.konrad.task_manager.repository.TaskRepository;
import com.konrad.task_manager.service.helpers.TaskHelperService;
import com.konrad.task_manager.service.TaskSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.konrad.task_manager.service.TaskSpecifications.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskSearchServiceImpl implements TaskSearchService {
    private static final Sort defaultSort = Sort.by("id").descending();

    private final TaskRepository taskRepository;

    public Page<TaskDto> find(Pageable pageable, TaskSearchCriteria taskSearchCriteria) {
        log.debug("Starting task search with criteria: {}", taskSearchCriteria);
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("id").descending());
        }

        Page<TaskEntity> taskEntities = findTasks(pageable, taskSearchCriteria);

        List<TaskDto> taskDtoList = new ArrayList<>();
                taskEntities.forEach(
                        taskEntity -> taskDtoList.add(TaskHelperService.mapTaskEntityToTaskDto(taskEntity))
                );
        log.debug("Task search completed. Found {} tasks.", taskEntities.getTotalElements());
        return new PageImpl<>(taskDtoList, taskEntities.getPageable(), taskEntities.getTotalElements());
    }

    private Page<TaskEntity> findTasks(Pageable pageable, TaskSearchCriteria taskSearchCriteria) {
        log.debug("Starting task search with criteria: {}", taskSearchCriteria);

        List<Specification<TaskEntity>> specifications = new ArrayList<>(4);
        addTitle(taskSearchCriteria.getTitle(), specifications);
        addDescription(taskSearchCriteria.getDescription(), specifications);
        addStatus(taskSearchCriteria.getStatus(), specifications);
        addDeadline(taskSearchCriteria.getDeadlineFrom(), taskSearchCriteria.getDeadlineTo(), specifications);

        log.debug("Number of specifications added: {}", specifications.size());

        Specification<TaskEntity> spec = combineSpecifications(specifications);
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }

        return taskRepository.findAll(spec, pageable);

    }

    private Specification<TaskEntity> combineSpecifications(List<Specification<TaskEntity>> specifications) {
        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }

    private void addTitle(String titleSearchText, List<Specification<TaskEntity>> specs) {
        if (StringUtils.isNotEmpty(titleSearchText)) {
            log.debug("Adding title specification for search text: {}", titleSearchText);
            specs.add(text(titleSearchText, "title"));

        }
    }

    private void addDescription(String descriptionSearchText, List<Specification<TaskEntity>> specs) {
        if (StringUtils.isNotEmpty(descriptionSearchText)) {
            log.debug("Adding description specification for search text: {}", descriptionSearchText);
            specs.add(text(descriptionSearchText, "description"));

        }
    }

    private void addDeadline(LocalDate deadlineFrom, LocalDate deadlineTo, List<Specification<TaskEntity>> specs) {
        if (deadlineFrom != null || deadlineTo != null) {
            LocalDate from = deadlineFrom != null ? deadlineFrom : LocalDate.parse("1900-01-01");
            LocalDate to = deadlineTo != null ? deadlineTo : LocalDate.parse("2300-12-31");
            log.debug("Adding deadline specification for range: {} to {}", from, to);
            specs.add(deadline(from, to, "deadline"));
        }
    }

    private void addStatus(String status, List<Specification<TaskEntity>> specs) {

        if ( StringUtils.isNotEmpty(status)) {
            var statusEnum = Status.valueOf(status);
            log.debug("Adding status specification for status: {}", statusEnum);
            specs.add(statusText(statusEnum, "status"));
        }
    }

}
