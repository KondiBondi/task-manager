package com.konrad.task_manager.model.criteria;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserSearchCriteria {
    private String fullName;
    private String email;
}