package com.school.science.fair.domain.dto;

import com.school.science.fair.domain.enumeration.GradeYearEnum;
import lombok.Data;

@Data
public class ClassDto {
    private Long id;
    private String name;
    private GradeYearEnum gradeYear;
    private boolean active;
}
