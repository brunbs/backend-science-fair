package com.school.science.fair.controller;

import com.school.science.fair.api.ScienceFairApi;
import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairListResponse;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.UpdateScienceFairRequest;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.UpdateScienceFairDto;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.service.ScienceFairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    public ResponseEntity<ScienceFairResponse> getScienceFair(Long id) {
        ScienceFairDto foundScienceFair = scienceFairService.getScienceFair(id);
        return ResponseEntity.ok().body(scienceFairMapper.dtoToResponse(foundScienceFair));
    }

    @Override
    public ResponseEntity<ScienceFairResponse> updateScienceFair(Long id, UpdateScienceFairRequest updateScienceFairRequest) {
        UpdateScienceFairDto scienceFairInfoToUpdate = scienceFairMapper.updateRequestToRequestDto(updateScienceFairRequest);
        ScienceFairDto updatedScienceFair = scienceFairService.updateScienceFair(id, scienceFairInfoToUpdate);
        return ResponseEntity.ok().body(scienceFairMapper.dtoToResponse(updatedScienceFair));
    }

    @Override
    public ResponseEntity<List<ScienceFairListResponse>> getAllScienceFairs() {
        List<ScienceFairDto> foundScienceFairs = scienceFairService.getAllScienceFairs();
        return ResponseEntity.ok().body(scienceFairMapper.listDtoToListResponse(foundScienceFairs));
    }

    @Override
    public ResponseEntity<ScienceFairResponse> deleteScienceFair(Long id) {
        ScienceFairDto deletedScienceFair = scienceFairService.deleteScienceFair(id);
        return ResponseEntity.ok().body(scienceFairMapper.dtoToResponse(deletedScienceFair));
    }

    @Override
    public ResponseEntity<List<ScienceFairListResponse>> getAllActiveScienceFairs() {
        List<ScienceFairDto> foundActiveScienceFairs = scienceFairService.getAllActiveScienceFairs();
        return ResponseEntity.ok().body(scienceFairMapper.listDtoToListResponse(foundActiveScienceFairs));
    }
}
