package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateProjectRequest;
import com.school.science.fair.domain.ProjectResponse;
import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.entity.IcProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface IcProjectMapper {

    ProjectDto entityToDto(IcProject entity);
    IcProject DtoToEntity(ProjectDto projectDto);
    IcProject createDtoToEntity(CreateProjectDto createProjectDto);
    CreateProjectDto createRequestToCreateDto (CreateProjectRequest createProjectRequest);
    ProjectResponse dtoToResponse(ProjectDto projectDto);
}
