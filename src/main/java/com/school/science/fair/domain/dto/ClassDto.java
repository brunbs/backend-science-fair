package com.school.science.fair.domain.dto;

import com.school.science.fair.domain.enumeration.GradeYearEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassDto {
    private Long id;
    private String name;
    private GradeYearEnum gradeYear;
    private boolean active;
}
