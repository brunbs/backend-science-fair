package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.CreateClassDto;

import java.util.List;

public interface ClassService {

    ClassDto createClass(CreateClassDto createClassDto);
    ClassDto getClass(Long id);
    List<ClassDto> getAllActiveClasses();

}
