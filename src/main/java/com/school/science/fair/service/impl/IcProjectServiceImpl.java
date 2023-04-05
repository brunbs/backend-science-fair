package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.*;
import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.ScienceFair;
import com.school.science.fair.domain.entity.Topic;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.repository.IcProjectRepository;
import com.school.science.fair.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<ProjectUserDto> studentsInProjectDto = projectUserService.insertUsersInProject(createProjectDto.getStudentsRegistrations(), createdProjectDto);

        List<GradeDto> gradesToAdd = gradeMapper.listEntityToListDto(foundScienceFair.getGradeSystem().getGrades());
        List<ProjectGradeDto> projectGrades = projectGradeService.insertGradesIntoProject(gradesToAdd, createdProjectDto);

        createdProjectDto.setStudents(studentsInProjectDto);
        createdProjectDto.setGrades(projectGrades);

        return createdProjectDto;
    }

    @Override
    public ProjectDto getProject(Long projectId) {
        IcProject foundProject = findProjectOrThrowException(projectId);
        ProjectDto projectToReturn = icProjectMapper.entityToDto(foundProject);
        List<ProjectUserDto> projectUsers = projectUserService.getProjectUsers(projectId);
        List<ProjectUserDto> projectStudents = new ArrayList<>();
        projectUsers.forEach(projectUser -> {
            if(projectUser.getRole().equals(UserTypeEnum.STUDENT)) {
                projectStudents.add(projectUser);
            } else if(projectUser.getRole().equals(UserTypeEnum.TEACHER)) {
                projectToReturn.setTeacher(projectUser);
            }
        });
        projectToReturn.setStudents(projectStudents);
        List<ProjectGradeDto> projectGrades = projectGradeService.getProjectGrades(projectId);
        projectToReturn.setGrades(projectGrades);
        projectToReturn.updateGradeSum();
        return projectToReturn;
    }

    private IcProject findProjectOrThrowException(Long projectId) {
        Optional<IcProject> foundProject = icProjectRepository.findById(projectId);
        if(foundProject.isEmpty()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.PROJECT_NOT_FOUND);
        }
        return foundProject.get();
    }
}
