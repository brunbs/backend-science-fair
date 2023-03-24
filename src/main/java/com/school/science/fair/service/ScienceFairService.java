package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;

public interface ScienceFairService {

    ScienceFairDto createScienceFair(ScienceFairRequestDto scienceFairRequestDto);
    ScienceFairDto getScienceFair(Long id);

}
