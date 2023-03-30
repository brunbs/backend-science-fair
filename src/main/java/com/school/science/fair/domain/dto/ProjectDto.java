package com.school.science.fair.domain.dto;

import com.school.science.fair.domain.entity.Topic;
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
    private List<UserProjectDto> students;
    private UserProjectDto teacher;
    private List<ProjectGradeDto> grades;
}
