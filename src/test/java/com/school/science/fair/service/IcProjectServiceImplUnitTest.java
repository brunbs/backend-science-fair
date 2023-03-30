package com.school.science.fair.service;

import com.school.science.fair.domain.dto.*;
import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.Topic;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.repository.IcProjectRepository;
import com.school.science.fair.service.impl.IcProjectServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.school.science.fair.domain.mother.AreaOfKnowledgeMother.getTopicEntity;
import static com.school.science.fair.domain.mother.IcProjectMother.getCreateProjectDto;
import static com.school.science.fair.domain.mother.IcProjectMother.getIcProjectEntity;
import static com.school.science.fair.domain.mother.ProjectGradeDtoMother.getProjectGradeDtos;
import static com.school.science.fair.domain.mother.ScienceFairMother.getScienceFairDto;
import static com.school.science.fair.domain.mother.UserProjectMother.getStudentsUserProjectDtoList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class IcProjectServiceImplUnitTest {

    @Spy
    private ScienceFairMapper scienceFairMapper = Mappers.getMapper(ScienceFairMapper.class);

    @Spy
    private IcProjectMapper icProjectMapper = Mappers.getMapper(IcProjectMapper.class);

    @Spy
    private GradeMapper gradeMapper = Mappers.getMapper(GradeMapper.class);

    @Mock
    private ScienceFairService scienceFairService;

    @Mock
    private AreaOfKnowledgeService areaOfKnowledgeService;

    @InjectMocks
    private IcProjectServiceImpl icProjectService;

    @Mock
    private IcProjectRepository icProjectRepository;

    @Mock
    private ProjectUserService projectUserService;

    @Mock
    private ProjectGradeService projectGradeService;
    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("Create project with valid science fair id and valid CreateProjectDto")
    @Test
    void givenValidScienceFairIdAndCreateProjectDtoWhenCreateProjectThenReturnsProjectDto() {
        ScienceFairDto foundScienceFair = getScienceFairDto();
        Topic foundTopic = getTopicEntity();
        CreateProjectDto createProjectDto = getCreateProjectDto();
        IcProject createdIcProjectEntity = getIcProjectEntity();
        List<UserProjectDto> userProjectDtos = getStudentsUserProjectDtoList();
        List<ProjectGradeDto> projectGradeDtos = getProjectGradeDtos();

        given(scienceFairService.getScienceFair(anyLong())).willReturn(foundScienceFair);
        given(areaOfKnowledgeService.getTopicOrThrowException(anyLong())).willReturn(foundTopic);
        given(icProjectRepository.save(any(IcProject.class))).willReturn(createdIcProjectEntity);
        given(projectUserService.insertUsersInProject(anyList(), any(ProjectDto.class))).willReturn(userProjectDtos);
        given(projectGradeService.insertGradesIntoProject(anyList(), any(ProjectDto.class))).willReturn(projectGradeDtos);

        ProjectDto createdProject = icProjectService.createProject(1l, createProjectDto);

        assertThat(createdProject.getTitle()).isEqualTo(createProjectDto.getTitle());
        assertThat(createdProject.getDescription()).isEqualTo(createProjectDto.getDescription());
        assertThat(createdProject.getTopic().getId()).isEqualTo(foundTopic.getId());
        assertThat(createdProject.getTopic().getName()).isEqualTo(foundTopic.getName());
        createdProject.getStudents().forEach(student -> assertThat(student).isIn(userProjectDtos));
        assertThat(createdProject.getTeacher()).isNull();
        createdProject.getGrades().forEach(grade -> assertThat(grade).isIn(projectGradeDtos));
    }

}
