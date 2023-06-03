package com.school.science.fair.service;

import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;

import java.util.List;

public interface IcProjectService {

    ProjectDto createProject(Long scienceFairId, CreateProjectDto createProjectDto);
    ProjectDto getProject(Long projectId);
    List<ProjectDto> getAllProjectsFromScienceFair(Long scienceFairId);
    ProjectDto deleteProject(Long projectId);

}
