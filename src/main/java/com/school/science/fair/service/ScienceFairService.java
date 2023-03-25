package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.UpdateScienceFairDto;

public interface ScienceFairService {

    ScienceFairDto createScienceFair(ScienceFairRequestDto scienceFairRequestDto);
    ScienceFairDto getScienceFair(Long id);
    ScienceFairDto updateScienceFair(Long id, UpdateScienceFairDto scienceFairDto);

}
