package com.school.science.fair.domain.dto;

import com.school.science.fair.domain.enumeration.GradeYearEnum;
import lombok.Data;

@Data
public class ClassRequestDto {

    private String name;
    private GradeYearEnum gradeYear;

}
