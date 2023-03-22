package com.school.science.fair.service;

import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;

import java.util.List;

public interface GradeSystemService {

    GradeSystemDto createGradeSystem(GradeSystemRequestDto gradeSystemRequestDto);
    GradeSystemDto getGradeSystem(Long id);
    List<GradeSystemDto> getAllGradeSystem();

}
