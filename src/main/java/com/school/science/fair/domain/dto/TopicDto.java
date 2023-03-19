package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicDto {

    private Long id;
    private String name;

}
