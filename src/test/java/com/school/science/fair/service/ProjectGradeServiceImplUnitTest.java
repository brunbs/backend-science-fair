package com.school.science.fair.service;

import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.ProjectGradeDto;
import com.school.science.fair.domain.entity.ProjectGrade;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.ProjectGradeMapper;
import com.school.science.fair.repository.ProjectGradeRepository;
import com.school.science.fair.service.impl.ProjectGradeServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.school.science.fair.domain.mother.GradeSystemMother.getGradeDtoList;
import static com.school.science.fair.domain.mother.IcProjectMother.getProjectDto;
import static com.school.science.fair.domain.mother.ProjectGradeMother.getProjectGrades;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class ProjectGradeServiceImplUnitTest {

    @Mock
    private ProjectGradeRepository projectGradeRepository;

    @InjectMocks
    private ProjectGradeServiceImpl projectGradeService;

    @Spy
    private GradeMapper gradeMapper = Mappers.getMapper(GradeMapper.class);

    @Spy
    private ProjectGradeMapper projectGradeMapper = Mappers.getMapper(ProjectGradeMapper.class);

    @Spy
    private IcProjectMapper icProjectMapper = Mappers.getMapper(IcProjectMapper.class);

    @DisplayName("Create Project Grade List")
    @Test
    void givenValidListOfProjectGradesAndValidProjectDtoWhenInsertGradesIntoProjectThenReturnsGradeDtoList() {
        ProjectDto projectDto = getProjectDto();
        List<GradeDto> gradeDtos = getGradeDtoList();

        given(projectGradeRepository.saveAll(anyList())).willReturn(List.of());

        List<ProjectGradeDto> projectGradeDtos = projectGradeService.insertGradesIntoProject(gradeDtos, projectDto);

        assertThat(projectGradeDtos).usingRecursiveComparison().ignoringFields("id", "gradeValue").isEqualTo(gradeDtos);

        projectGradeDtos.forEach(grade -> {
            assertThat(grade.getGradeValue()).isEqualTo(0.0);
        });

    }

    @DisplayName("Get a project grades")
    @Test
    void givenValidProjectWhenGetProjectGradesThenReturnsGradeDtoList() {
        List<ProjectGrade> projectGrades = getProjectGrades();
        given(projectGradeRepository.findAllByIdIcProjectId(anyLong())).willReturn(projectGrades);

        List<ProjectGradeDto> projectGradeDtos = projectGradeService.getProjectGrades(1l);
        for(int i = 0; i < projectGradeDtos.size(); i++) {
            assertThat(projectGradeDtos.get(i).getGradeValue()).isEqualTo(projectGrades.get(i).getGradeValue());
            assertThat(projectGradeDtos.get(i).getName()).isEqualTo(projectGrades.get(i).getGradeName());
            assertThat(projectGradeDtos.get(i).getDescription()).isEqualTo(projectGrades.get(i).getGradeDescription());
        }
    }

    @DisplayName("Delete all grades of a project")
    @Test
    void givenValidProjectIdWhenDeleteAllProjectGradesThenDeletesAllGrades() {
        List<ProjectGrade> projectGrades = getProjectGrades();
        given(projectGradeRepository.findAllByIdIcProjectId(anyLong())).willReturn(projectGrades);
        doNothing().when(projectGradeRepository).deleteAll(projectGrades);
        projectGradeService.deleteAllProjectGrades(1l);
        verify(projectGradeRepository).findAllByIdIcProjectId(anyLong());
        verify(projectGradeRepository).deleteAll(projectGrades);
    }

}
