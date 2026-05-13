package com.sprintly.backend.repository;

import com.sprintly.backend.entity.ProjectEntity;
import com.sprintly.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository
        extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByCreatedBy(UserEntity user);
}