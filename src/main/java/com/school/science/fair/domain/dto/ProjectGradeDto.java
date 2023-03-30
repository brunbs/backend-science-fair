package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectGradeDto {

    private String name;
    private String description;
    private Double maxValue;
    private Double gradeValue;

}
