package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.dto.ProjectGradeDto;

import java.util.List;

public class ProjectGradeDtoMother {

    public static List<ProjectGradeDto> getProjectGradeDtos() {
        return List.of(
                ProjectGradeDto.builder().name("Grade A").gradeValue(0.0).maxValue(0.5).description("Grade A Description").build(),
                ProjectGradeDto.builder().name("Grade B").gradeValue(0.0).maxValue(0.1).description("Grade B Description").build()
        );
    }

}
