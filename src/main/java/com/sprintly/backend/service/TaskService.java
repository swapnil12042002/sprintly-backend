package com.sprintly.backend.service;

import com.sprintly.backend.dto.CreateTaskRequest;
import com.sprintly.backend.dto.TaskResponse;
import com.sprintly.backend.dto.UpdateTaskRequest;
import com.sprintly.backend.entity.ProjectEntity;
import com.sprintly.backend.entity.TaskEntity;
import com.sprintly.backend.entity.UserEntity;
import com.sprintly.backend.repository.ProjectRepository;
import com.sprintly.backend.repository.TaskRepository;
import com.sprintly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public String createTask(
            Long projectId,
            CreateTaskRequest request,
            String email
    ) {

        UserEntity currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // member validation
        if (!project.getMembers().contains(currentUser)
                && !project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not member of this project"
            );
        }

        UserEntity assignedUser = userRepository
                .findById(request.getAssignedUserId())
                .orElseThrow(() ->
                        new RuntimeException("Assigned user not found"));

        TaskEntity task = new TaskEntity();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());

        task.setStatus("TODO");

        task.setProject(project);
        task.setAssignedTo(assignedUser);
        task.setCreatedBy(currentUser);

        taskRepository.save(task);

        return "Task created successfully";
    }

    public List<TaskResponse> getTasks(
            Long projectId,
            String email
    ) {

        UserEntity currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // authorization
        if (!project.getMembers().contains(currentUser)
                && !project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not member of this project"
            );
        }

        List<TaskEntity> tasks =
                taskRepository.findByProject(project);

        return tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getAssignedTo() != null
                                ? task.getAssignedTo().getEmail()
                                : null
                ))
                .toList();
    }
    public String updateTask(
            Long taskId,
            UpdateTaskRequest request,
            String email
    ) {

        UserEntity currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        TaskEntity task = taskRepository
                .findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        ProjectEntity project = task.getProject();

        // authorization
        if (!project.getMembers().contains(currentUser)
                && !project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not member of this project"
            );
        }

        // partial updates
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getAssignedUserId() != null) {

            UserEntity assignedUser = userRepository
                    .findById(request.getAssignedUserId())
                    .orElseThrow(() ->
                            new RuntimeException("Assigned user not found"));

            task.setAssignedTo(assignedUser);
        }

        taskRepository.save(task);

        return "Task updated successfully";
    }
    public String deleteTask(
            Long taskId,
            String email
    ) {

        UserEntity currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        TaskEntity task = taskRepository
                .findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        ProjectEntity project = task.getProject();

        // authorization
        if (!project.getMembers().contains(currentUser)
                && !project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not member of this project"
            );
        }

        taskRepository.delete(task);

        return "Task deleted successfully";
    }
}