package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.*;
import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.ScienceFair;
import com.school.science.fair.domain.entity.Topic;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.repository.IcProjectRepository;
import com.school.science.fair.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IcProjectServiceImpl implements IcProjectService {

    @Autowired
    private AreaOfKnowledgeService areaOfKnowledgeService;
    @Autowired
    private IcProjectRepository icProjectRepository;
    @Autowired
    private ScienceFairMapper scienceFairMapper;
    @Autowired
    private ScienceFairService scienceFairService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private IcProjectMapper icProjectMapper;
    @Autowired
    private ProjectGradeService projectGradeService;
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    @Transactional
    public ProjectDto createProject(Long scienceFairId, CreateProjectDto createProjectDto) {

        ScienceFair foundScienceFair = scienceFairMapper.dtoToEntity(scienceFairService.getScienceFair(scienceFairId));
        Topic projectTopic = areaOfKnowledgeService.getTopicOrThrowException(createProjectDto.getTopicId());

        IcProject projectToCreate = icProjectMapper.createDtoToEntity(createProjectDto);
        projectToCreate.setTopic(projectTopic);
        projectToCreate.setScienceFair(foundScienceFair);

        IcProject createdIcProject = icProjectRepository.save(projectToCreate);
        ProjectDto createdProjectDto = icProjectMapper.entityToDto(createdIcProject);

        List<UserProjectDto> studentsInProjectDto = projectUserService.insertUsersInProject(createProjectDto.getStudentsRegistrations(), createdProjectDto);

        List<GradeDto> gradesToAdd = gradeMapper.listEntityToListDto(foundScienceFair.getGradeSystem().getGrades());
        List<ProjectGradeDto> projectGrades = projectGradeService.insertGradesIntoProject(gradesToAdd, createdProjectDto);

        createdProjectDto.setStudents(studentsInProjectDto);
        createdProjectDto.setGrades(projectGrades);

        return createdProjectDto;
    }
}
