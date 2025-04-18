package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.criteria.UserSearchCriteria;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.service.UserSearchService;
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

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class UserSearchServiceImplTest {

    @Autowired
    private UserSearchService userSearchService;

    @Test
    void find_shouldReturnFilteredTasks_matchingFullName() {
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .fullName("Peter Griffin")
                .build();
        Page<UserDto> page = expectSingleMatchingTask(userSearchCriteria);
        UserDto te1 = page.getContent().get(0);
        assertThat(te1.getId()).isEqualTo(1);
    }

    @Test
    void find_shouldReturnFilteredTasks_matchingEmail() {
        UserSearchCriteria userSearchCriteria = UserSearchCriteria.builder()
                .email("scofield")
                .build();
        Page<UserDto> page = expectSingleMatchingTask(userSearchCriteria);
        UserDto te1 = page.getContent().get(0);
        assertThat(te1.getId()).isEqualTo(5);
    }


    private Page<UserDto> expectSingleMatchingTask(UserSearchCriteria searchCriteria) {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<UserDto> page = userSearchService.find(pageable, searchCriteria);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getNumberOfElements()).isEqualTo(1);
        return page;
    }

}