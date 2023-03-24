package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScienceFairDto {

    private Long id;
    private String name;
    private String description;
    private int editionYear;
    private GradeSystemDto gradeSystem;

}
