package com.sprintly.backend.service;

import com.sprintly.backend.dto.CreateProjectRequest;
import com.sprintly.backend.dto.InviteRequest;
import com.sprintly.backend.dto.MemberResponse;
import com.sprintly.backend.dto.ProjectResponse;
import com.sprintly.backend.entity.ProjectEntity;
import com.sprintly.backend.entity.UserEntity;
import com.sprintly.backend.repository.ProjectRepository;
import com.sprintly.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public String createProject(
            CreateProjectRequest request,
            String email
    ) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProjectEntity project = new ProjectEntity();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(user);

        projectRepository.save(project);

        return "Project created successfully";
    }

    public List<ProjectResponse> getProjects(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<ProjectEntity> projects =
                projectRepository.findByCreatedBy(user);

        return projects.stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        project.getDescription()
                ))
                .toList();
    }

    public String deleteProject(
            Long projectId,
            String email
    ) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        if (!project.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        projectRepository.delete(project);

        return "Project deleted successfully";
    }

    public String inviteUser(
            Long projectId,
            InviteRequest request,
            String currentUserEmail
    ) {

        UserEntity currentUser = userRepository
                .findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found"));

        // only owner can invite
        if (!project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException("Unauthorized");
        }

        UserEntity invitedUser = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invited user not found"));

        // prevent duplicate invite
        if (project.getMembers().contains(invitedUser)) {
            return "User already added";
        }

        project.getMembers().add(invitedUser);

        projectRepository.save(project);

        return "User invited successfully";
    }

    public List<MemberResponse> getProjectMembers(
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

        // only owner allowed
        if (!project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException("Unauthorized");
        }

        return project.getMembers()
                .stream()
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getName(),
                        member.getEmail()
                ))
                .toList();
    }

    public String removeMember(
            Long projectId,
            Long userId,
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

        // only owner allowed
        if (!project.getCreatedBy()
                .getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException("Unauthorized");
        }

        UserEntity member = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Member not found"));

        project.getMembers().remove(member);

        projectRepository.save(project);

        return "Member removed successfully";
    }
}