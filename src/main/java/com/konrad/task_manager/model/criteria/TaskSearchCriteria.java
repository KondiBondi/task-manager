package com.konrad.task_manager.model.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TaskSearchCriteria {
    private String title;
    private String description;
    private String status;
    private LocalDate deadlineFrom;
    private LocalDate deadlineTo;
}