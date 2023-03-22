package com.school.science.fair.service;

import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;

public interface GradeSystemService {

    GradeSystemDto createGradeSystem(GradeSystemRequestDto gradeSystemRequestDto);
    GradeSystemDto getGradeSystem(Long id);

}
