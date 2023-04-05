package com.school.science.fair.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;
    private String title;
    private String description;
    private TopicDto topic;
    private List<ProjectUserDto> students;
    private ProjectUserDto teacher;
    private List<ProjectGradeDto> grades;
    private Double gradeSum;

    public void updateGradeSum() {
        Double gradeSum = 0.0;
        for(ProjectGradeDto grade : this.grades) {
            gradeSum += grade.getGradeValue();
        }
        this.gradeSum = gradeSum;
    }

}
