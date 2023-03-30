package com.school.science.fair.service;

import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;

public interface IcProjectService {

    ProjectDto createProject(Long scienceFairId, CreateProjectDto createProjectDto);

}
