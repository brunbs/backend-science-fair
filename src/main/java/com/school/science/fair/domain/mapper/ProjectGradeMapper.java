package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.dto.ProjectGradeDto;
import com.school.science.fair.domain.entity.ProjectGrade;
import com.school.science.fair.domain.entity.ProjectGradePK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface ProjectGradeMapper {

    @Mapping(target = "name", expression = "java(entity.getGradeName())")
    @Mapping(target = "description", expression = "java(entity.getGradeDescription())")
    ProjectGradeDto entityToDto(ProjectGrade entity);

    List<ProjectGradeDto> listEntityToListDto(List<ProjectGrade> entities);
}
