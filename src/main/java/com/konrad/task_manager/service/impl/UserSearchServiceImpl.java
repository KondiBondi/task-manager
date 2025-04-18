package com.konrad.task_manager.service.impl;

import com.konrad.task_manager.model.criteria.UserSearchCriteria;
import com.konrad.task_manager.model.dto.UserDto;
import com.konrad.task_manager.model.entity.UserEntity;
import com.konrad.task_manager.repository.UserRepository;
import com.konrad.task_manager.service.helpers.UserHelperService;
import com.konrad.task_manager.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.konrad.task_manager.service.UserSpecifications.email;
import static com.konrad.task_manager.service.UserSpecifications.fullame;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSearchServiceImpl implements UserSearchService {
    private static final Sort defaultSort = Sort.by("id").descending();

    private final UserRepository userRepository;


    @Override
    public Page<UserDto> find(Pageable pageable, UserSearchCriteria userSearchCriteria) {
        log.debug("Starting user search with criteria: {}", userSearchCriteria);
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("id").descending());
        }

        Page<UserEntity> userEntities = findUsers(pageable, userSearchCriteria);

        List<UserDto> userDtoList = new ArrayList<>();
        userEntities.forEach(
                userEntity -> userDtoList.add(UserHelperService.mapUserEntityToUserDto(userEntity))
        );
        log.debug("User search completed. Found {} users.", userEntities.getTotalElements());
        return new PageImpl<>(userDtoList, userEntities.getPageable(), userEntities.getTotalElements());
    }

    private Page<UserEntity> findUsers(Pageable pageable, UserSearchCriteria userSearchCriteria) {
        log.debug("Building specifications for user search criteria: {}", userSearchCriteria);
        List<Specification<UserEntity>> specifications = new ArrayList<>(2);
        addFullName(userSearchCriteria.getFullName(), specifications);
        addEmail(userSearchCriteria.getEmail(), specifications);

        log.debug("Number of specifications added: {}", specifications.size());

        Specification<UserEntity> spec = combineSpecifications(specifications);

        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }

        return userRepository.findAll(spec, pageable);
    }

    private Specification<UserEntity> combineSpecifications(List<Specification<UserEntity>> specifications) {
        return specifications.stream()
                .reduce(Specification::and)
                .orElse(null);
    }

    private void addFullName(String fullNameSearchText, List<Specification<UserEntity>> specs) {
        if (StringUtils.isNotEmpty(fullNameSearchText)) {
            log.debug("Adding full name specification for search text: {}", fullNameSearchText);
            specs.add(fullame(fullNameSearchText));
        }
    }

    private void addEmail(String fullEmail, List<Specification<UserEntity>> specs) {
        if (StringUtils.isNotEmpty(fullEmail)) {
            log.debug("Adding email specification for search text: {}", fullEmail);
            specs.add(email(fullEmail));

        }
    }
}
