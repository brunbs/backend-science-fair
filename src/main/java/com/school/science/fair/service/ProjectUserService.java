package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.UserProjectDto;

import java.util.List;

public interface ProjectUserService {

    List<UserProjectDto> insertUsersInProject(List<Long> usersRegistrations, ProjectDto projectDto);

}
