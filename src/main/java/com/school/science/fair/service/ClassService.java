package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;

import java.util.List;

public interface ClassService {

    ClassDto createClass(ClassRequestDto classRequestDto);
    ClassDto getClass(Long classId);
    List<ClassDto> getAllActiveClasses();
    ClassDto deleteClass(Long classId);

    ClassDto updateClass(Long classId, ClassRequestDto classRequestDto);

}
