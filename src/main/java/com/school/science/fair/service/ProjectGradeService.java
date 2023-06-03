package com.school.science.fair.service;

import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.ProjectGradeDto;

import java.util.List;

public interface ProjectGradeService {

    List<ProjectGradeDto> insertGradesIntoProject(List<GradeDto> grades, ProjectDto projectDto);
    List<ProjectGradeDto> getProjectGrades(Long projectId);
    void deleteAllProjectGrades(Long projectId);

}
