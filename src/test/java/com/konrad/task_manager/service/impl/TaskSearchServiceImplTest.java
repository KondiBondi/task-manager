package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.criteria.TaskSearchCriteria;
import com.konrad.task_manager.model.dto.TaskDto;
import com.konrad.task_manager.service.TaskSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TaskSearchServiceImplTest {

    @Autowired
    private TaskSearchService taskSearchService;

    @Test
    void find_shouldReturnFilteredTasks_matchingTitle() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .title("Review")
                .build();
        Page<TaskDto> page = expectSingleMatchingTask(taskSearchCriteria);
        TaskDto te1 = page.getContent().get(0);
        assertThat(te1.getId()).isEqualTo(2);
    }

    @Test
    void find_shouldReturnFilteredTasks_matchingDescription() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .description("authentication")
                .build();
        Page<TaskDto> page = expectSingleMatchingTask(taskSearchCriteria);
        TaskDto te1 = page.getContent().get(0);
        assertThat(te1.getId()).isEqualTo(2);
    }

    @Test
    void find_shouldReturnFilteredTasks_matchingStatus() {
        TaskSearchCriteria taskSearchCriteria = TaskSearchCriteria.builder()
                .status("IN_PROGRESS")
                .build();
        Page<TaskDto> page = expectSingleMatchingTask(taskSearchCriteria);
        TaskDto te1 = page.getContent().get(0);
        assertThat(te1.getId()).isEqualTo(1);
    }

    @Test
    void fine_shouldReturnFilteredTasks_matchingDeadlineRange() {
        TaskSearchCriteria searchCriteria = TaskSearchCriteria.builder()
                .deadlineFrom(LocalDate.parse("2023-01-01"))
                .deadlineTo(LocalDate.parse("2024-01-01"))
                .build();

        Page<TaskDto> page = expectSingleMatchingTask(searchCriteria);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(1L);

        // Repeat with empty To Date
        searchCriteria = TaskSearchCriteria.builder()
                .deadlineFrom(LocalDate.parse("2025-01-31")).build();

        page = taskSearchService.find(PageRequest.of(0, 10), searchCriteria);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(3L);

        // Repeat with empty From Date
        searchCriteria = TaskSearchCriteria.builder()
                .deadlineTo(LocalDate.parse("2024-01-05")).build();

        page = expectSingleMatchingTask(searchCriteria);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getId()).isEqualTo(1L);


    }

    private Page<TaskDto> expectSingleMatchingTask(TaskSearchCriteria searchCriteria) {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<TaskDto> page = taskSearchService.find(pageable, searchCriteria);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        return page;
    }



}