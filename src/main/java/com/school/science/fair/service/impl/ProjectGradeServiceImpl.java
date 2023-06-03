package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.ProjectGradeDto;
import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.ProjectGrade;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.ProjectGradeMapper;
import com.school.science.fair.repository.ProjectGradeRepository;
import com.school.science.fair.service.ProjectGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectGradeServiceImpl implements ProjectGradeService {

    @Autowired
    private IcProjectMapper icProjectMapper;

    @Autowired
    private ProjectGradeRepository projectGradeRepository;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private ProjectGradeMapper projectGradeMapper;

    @Override
    public List<ProjectGradeDto> insertGradesIntoProject(List<GradeDto> grades, ProjectDto projectDto) {

        IcProject project = icProjectMapper.DtoToEntity(projectDto);

        List<ProjectGrade> projectGrades = new ArrayList<>();

        for(GradeDto grade : grades) {
            ProjectGrade newProjectGrade = new ProjectGrade(project, gradeMapper.requestDtoToEntity(grade));
            projectGrades.add(newProjectGrade);
        }

        projectGradeRepository.saveAll(projectGrades);

        List<ProjectGradeDto> projectGradeDtos = projectGradeMapper.listEntityToListDto(projectGrades);

        return projectGradeDtos;
    }

    @Override
    public List<ProjectGradeDto> getProjectGrades(Long projectId) {
        List<ProjectGrade> projectGrades = projectGradeRepository.findAllByIdIcProjectId(projectId);
        return projectGradeMapper.listEntityToListDto(projectGrades);
    }

    @Override
    public void deleteAllProjectGrades(Long projectId) {
        List<ProjectGrade> projectGrades = projectGradeRepository.findAllByIdIcProjectId(projectId);
        projectGradeRepository.deleteAll(projectGrades);
    }
}
