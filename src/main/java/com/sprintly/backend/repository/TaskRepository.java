package com.sprintly.backend.repository;

import com.sprintly.backend.entity.ProjectEntity;
import com.sprintly.backend.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByProject(ProjectEntity project);
}