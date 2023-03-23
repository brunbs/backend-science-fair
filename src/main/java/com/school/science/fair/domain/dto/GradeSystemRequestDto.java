package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GradeSystemRequestDto {

    private String name;
    private String description;
    private Double maxValue;
    private List<GradeDto> grades;

}
