package com.sprintly.backend.controller;

import com.sprintly.backend.dto.CreateProjectRequest;
import com.sprintly.backend.dto.InviteRequest;
import com.sprintly.backend.dto.MemberResponse;
import com.sprintly.backend.dto.ProjectResponse;
import com.sprintly.backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public String createProject(
            @RequestBody CreateProjectRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.createProject(request, email);
    }

    @GetMapping
    public List<ProjectResponse> getProjects(
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.getProjects(email);
    }

    @DeleteMapping("/{projectId}")
    public String deleteProject(
            @PathVariable Long projectId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.deleteProject(projectId, email);
    }

    @PostMapping("/{projectId}/invite")
    public String inviteUser(
            @PathVariable Long projectId,
            @RequestBody InviteRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.inviteUser(
                projectId,
                request,
                email
        );
    }

    @GetMapping("/{projectId}/members")
    public List<MemberResponse> getMembers(
            @PathVariable Long projectId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.getProjectMembers(
                projectId,
                email
        );
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public String removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            Authentication authentication
    ) {

        String email = authentication.getName();

        return projectService.removeMember(
                projectId,
                userId,
                email
        );
    }
}