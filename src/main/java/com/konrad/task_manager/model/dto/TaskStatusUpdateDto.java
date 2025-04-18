package com.konrad.task_manager.model.dto;

import com.konrad.task_manager.model.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class TaskStatusUpdateDto {
    private Status status;
}