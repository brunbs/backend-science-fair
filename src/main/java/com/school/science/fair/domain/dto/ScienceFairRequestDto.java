package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ScienceFairRequestDto {

    private String name;
    private String description;
    private int editionYear;
    private Long gradeSystemId;

}
