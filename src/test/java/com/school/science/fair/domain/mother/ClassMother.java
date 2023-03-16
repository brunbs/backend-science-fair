package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.GradeYearEnum;

public class ClassMother {

    public static Class getClassEntity() {
        return Class.builder()
                .id(1l)
                .name("Class A")
                .gradeYear(GradeYearEnum.EM_1ANO)
                .active(true)
                .build();
    }

    public static ClassRequestDto getCreateClassRequestDto() {
        return ClassRequestDto.builder()
                .name("Class A")
                .gradeYear(GradeYearEnum.EM_1ANO)
                .build();
    }

    public static ClassDto getClassDto() {
        return ClassDto.builder()
                .id(1l)
                .active(true)
                .gradeYear(GradeYearEnum.EM_1ANO)
                .name("Class A")
                .build();
    }

    public static ClassRequestDto getUpdateClassRequestDto() {
        return ClassRequestDto.builder()
                .name("Class B")
                .build();
    }

}
