package com.sprintly.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    private String title;

    private String description;

    private String status;

    private String priority;

    private Long assignedUserId;
}