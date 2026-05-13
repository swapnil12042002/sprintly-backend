package com.sprintly.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private String status;

    private String priority;

    private String assignedTo;
}