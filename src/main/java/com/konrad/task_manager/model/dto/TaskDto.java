package com.konrad.task_manager.model.dto;

import com.konrad.task_manager.model.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDate deadline;
    private Set<UserDto> assignedUsers;
    private Integer version;
}
