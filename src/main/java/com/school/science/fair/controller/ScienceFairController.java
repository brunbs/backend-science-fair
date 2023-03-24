package com.school.science.fair.controller;

import com.school.science.fair.api.ScienceFairApi;
import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.service.ScienceFairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScienceFairController implements ScienceFairApi {

    @Autowired
    private ScienceFairService scienceFairService;

    @Autowired
    private ScienceFairMapper scienceFairMapper;

    @Override
    public ResponseEntity<ScienceFairResponse> createScienceFair(CreateScienceFairRequest createScienceFairRequest) {
        ScienceFairRequestDto scienceFairToCreate = scienceFairMapper.createRequestToRequestDto(createScienceFairRequest);
        ScienceFairDto createdScienceFair = scienceFairService.createScienceFair(scienceFairToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(scienceFairMapper.dtoToResponse(createdScienceFair));
    }
}
