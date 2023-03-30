package com.school.science.fair.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {

    private String title;
    private String description;
    private Long topicId;
    private List<Long> studentsRegistrations;
}
