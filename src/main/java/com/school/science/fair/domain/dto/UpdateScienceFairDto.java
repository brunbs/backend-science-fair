package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateScienceFairDto {

    private String name;
    private String description;

}
