package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AreaOfKnowledgeDto {

    private Long id;
    private String name;
    private List<TopicDto> topics;
    private boolean active;

}
