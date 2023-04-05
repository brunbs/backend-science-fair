package com.school.science.fair.controller;

import com.school.science.fair.api.ProjectApi;
import com.school.science.fair.domain.CreateProjectRequest;
import com.school.science.fair.domain.ProjectResponse;
import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.service.IcProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController implements ProjectApi {

    @Autowired
    private IcProjectService icProjectService;

    @Autowired
    private IcProjectMapper icProjectMapper;

    @Override
    public ResponseEntity<ProjectResponse> createProject(Long scienceFairId, CreateProjectRequest createProjectRequest) {
        CreateProjectDto projectToCreate = icProjectMapper.createRequestToCreateDto(createProjectRequest);
        ProjectDto createdProject = icProjectService.createProject(scienceFairId, projectToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(icProjectMapper.dtoToResponse(createdProject));
    }

    @Override
    public ResponseEntity<ProjectResponse> getProject(Long projectId) {
        ProjectDto foundProject = icProjectService.getProject(projectId);
        return ResponseEntity.ok().body(icProjectMapper.dtoToResponse(foundProject));
    }
}
