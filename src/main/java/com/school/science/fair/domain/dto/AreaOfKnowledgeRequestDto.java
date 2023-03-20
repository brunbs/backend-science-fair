package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AreaOfKnowledgeRequestDto {

    private String name;
    private List<TopicDto> topics;

}
