package com.school.science.fair.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentRequestDto {

    private Long registration;
    private String name;
    private String email;

}
