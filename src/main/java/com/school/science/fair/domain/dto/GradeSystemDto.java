package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GradeSystemDto {

    private Long id;
    private String name;
    private String description;
    private Double maxValue;
    private boolean active;
    private List<GradeDto> grades;

}
