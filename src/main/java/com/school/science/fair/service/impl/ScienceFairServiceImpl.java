package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.UpdateScienceFairDto;
import com.school.science.fair.domain.entity.ScienceFair;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.repository.ScienceFairRepository;
import com.school.science.fair.service.GradeSystemService;
import com.school.science.fair.service.ScienceFairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ScienceFairServiceImpl implements ScienceFairService {

    @Autowired
    private ScienceFairRepository scienceFairRepository;

    @Autowired
    private GradeSystemService gradeSystemService;

    @Autowired
    private ScienceFairMapper scienceFairMapper;

    @Autowired
    private GradeSystemMapper gradeSystemMapper;

    @Override
    public ScienceFairDto createScienceFair(ScienceFairRequestDto scienceFairRequestDto) {
        ScienceFair scienceFairToCreate = scienceFairMapper.requestDtoToEntity(scienceFairRequestDto);
        GradeSystemDto gradeSystemDto = gradeSystemService.getGradeSystem(scienceFairRequestDto.getGradeSystemId());
        scienceFairToCreate.setGradeSystem(gradeSystemMapper.dtoToEntity(gradeSystemDto));
        ScienceFair createdScienceFair = scienceFairRepository.save(scienceFairToCreate);
        return scienceFairMapper.entityToDto(createdScienceFair);
    }

    @Override
    public ScienceFairDto getScienceFair(Long id) {
        return scienceFairMapper.entityToDto(findScienceFairOrThrowException(id));
    }

    @Override
    public ScienceFairDto updateScienceFair(Long id, UpdateScienceFairDto updateScienceFairDto) {
        ScienceFair foundScienceFair = findScienceFairOrThrowException(id);
        scienceFairMapper.updateModelFromDto(updateScienceFairDto, foundScienceFair);
        ScienceFair updatedScienceFair = scienceFairRepository.save(foundScienceFair);
        return scienceFairMapper.entityToDto(updatedScienceFair);
    }

    private ScienceFair findScienceFairOrThrowException(Long id) {
        Optional<ScienceFair> foundScienceFair = scienceFairRepository.findById(id);
        if(foundScienceFair.isPresent()) {
            return foundScienceFair.get();
        }
        throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.SCIENCE_FAIR_NOT_FOUND);
    }
}
