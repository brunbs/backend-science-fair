package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.UpdateClassRequest;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.GradeYearEnum;
import org.hibernate.sql.Update;

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

    public static UpdateClassRequest getUpdateClassRequest() {
        return UpdateClassRequest.builder()
                .name("New name")
                .build();
    }

    public static CreateClassRequest getCreateClassRequest() {
        return CreateClassRequest.builder()
                .name("Class A")
                .gradeYear(CreateClassRequest.GradeYearEnum.EM_1ANO)
                .build();
    }

    public static ClassResponse getClassResponse() {
        return ClassResponse.builder()
                .name("Class A")
                .gradeYear(GradeYearEnum.EM_1ANO.getDescription())
                .active(true)
                .id(1l)
                .build();
    }

}
