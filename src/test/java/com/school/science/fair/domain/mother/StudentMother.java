package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateStudentRequest;
import com.school.science.fair.domain.StudentResponse;
import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.entity.Student;

public class StudentMother {

    public static Student getStudentEntity() {
        return Student.builder()
                .registration(1l)
                .name("Student A")
                .email("student@email.com")
                .active(true)
                .password("1")
                .build();
    }

    public static StudentRequestDto getStudentRequestDto() {
        return StudentRequestDto.builder()
                .name("Student A")
                .email("student@email.com")
                .registration(1l)
                .build();
    }

    public static StudentDto getStudentDto() {
        return StudentDto.builder()
                .registration(1l)
                .email("student@email.com")
                .name("Student A")
                .password("1")
                .active(true)
                .build();
    }

    public static CreateStudentRequest getCreateStudentRequest() {
        return CreateStudentRequest.builder()
                .registration(1l)
                .name("Student A")
                .email("student@email.com")
                .build();
    }

    public static StudentResponse getStudentResponse() {
        return StudentResponse.builder()
                .active(true)
                .email("student@email.com")
                .registration(1l)
                .name("Student A")
                .build();
    }

}
