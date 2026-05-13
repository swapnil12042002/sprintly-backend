package com.sprintly.backend.controller;

import com.sprintly.backend.dto.CreateTaskRequest;
import com.sprintly.backend.dto.TaskResponse;
import com.sprintly.backend.dto.UpdateTaskRequest;
import com.sprintly.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{projectId}/tasks")
    public String createTask(
            @PathVariable Long projectId,
            @RequestBody CreateTaskRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return taskService.createTask(
                projectId,
                request,
                email
        );
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskResponse> getTasks(
            @PathVariable Long projectId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return taskService.getTasks(
                projectId,
                email
        );
    }

    @PatchMapping("/tasks/{taskId}")
    public String updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return taskService.updateTask(
                taskId,
                request,
                email
        );
    }
    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return taskService.deleteTask(
                taskId,
                email
        );
    }
}