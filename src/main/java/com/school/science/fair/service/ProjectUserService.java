package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.ProjectUserDto;

import java.util.List;

public interface ProjectUserService {

    List<ProjectUserDto> insertUsersInProject(List<Long> usersRegistrations, ProjectDto projectDto);
    List<ProjectUserDto> getProjectUsers(Long projectId);

}
