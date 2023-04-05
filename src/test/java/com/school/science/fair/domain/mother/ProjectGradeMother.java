package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.dto.ProjectGradeDto;
import com.school.science.fair.domain.entity.ProjectGrade;

import java.util.List;

import static com.school.science.fair.domain.mother.GradeSystemMother.getGradesEntities;
import static com.school.science.fair.domain.mother.IcProjectMother.getIcProjectEntity;

public class ProjectGradeMother {

    public static List<ProjectGradeDto> getProjectGradeDtos() {
        return List.of(
                ProjectGradeDto.builder().name("Grade A").gradeValue(0.2).maxValue(0.5).description("Grade A Description").build(),
                ProjectGradeDto.builder().name("Grade B").gradeValue(0.1).maxValue(0.1).description("Grade B Description").build()
        );
    }

    public static List<ProjectGrade> getProjectGrades() {
        ProjectGrade projectGradeA = new ProjectGrade(getIcProjectEntity(), getGradesEntities().get(0));
        projectGradeA.setGradeValue(0.2);
        ProjectGrade projectGradeB = new ProjectGrade(getIcProjectEntity(), getGradesEntities().get(1));
        projectGradeA.setGradeValue(0.1);
        return List.of(
                projectGradeA, projectGradeB
        );
    }

}
