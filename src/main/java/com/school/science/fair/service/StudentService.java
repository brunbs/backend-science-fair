package com.school.science.fair.service;

import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;

public interface StudentService {

    StudentDto createStudent(StudentRequestDto createStudentDto);
    StudentDto getStudent(Long studentRegistration);

}
