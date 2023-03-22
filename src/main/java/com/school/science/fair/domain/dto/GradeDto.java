package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradeDto {

    private Long id;
    private String name;
    private String description;
    private Double maxValue;

}
