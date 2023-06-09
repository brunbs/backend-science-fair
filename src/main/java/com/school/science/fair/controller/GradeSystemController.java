package com.school.science.fair.controller;

import com.school.science.fair.api.GradeSystemApi;
import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.GradeSystemResponse;
import com.school.science.fair.domain.UpdateGradeSystemRequest;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.service.GradeSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GradeSystemController implements GradeSystemApi {

    @Autowired
    private GradeSystemService gradeSystemService;

    @Autowired
    private GradeSystemMapper gradeSystemMapper;

    @Override
    public ResponseEntity<GradeSystemResponse> createGradeSystem(CreateGradeSystemRequest createGradeSystemRequest) {
        GradeSystemRequestDto gradeSystemToCreate = gradeSystemMapper.requestToRequestDto(createGradeSystemRequest);
        GradeSystemDto gradeSystemDto = gradeSystemService.createGradeSystem(gradeSystemToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeSystemMapper.dtoToResponse(gradeSystemDto));
    }

    @Override
    public ResponseEntity<GradeSystemResponse> getGradeSystem(Long id) {
        GradeSystemDto foundGradeSystem = gradeSystemService.getGradeSystem(id);
        return ResponseEntity.ok().body(gradeSystemMapper.dtoToResponse(foundGradeSystem));
    }

    @Override
    public ResponseEntity<List<GradeSystemResponse>> getAllGradeSystem() {
        List<GradeSystemDto> foundGradeSystems = gradeSystemService.getAllGradeSystem();
        return ResponseEntity.ok().body(gradeSystemMapper.listDtoToListResponse(foundGradeSystems));
    }

    @Override
    public ResponseEntity<GradeSystemResponse> updateGradeSystem(Long id, UpdateGradeSystemRequest updateGradeSystemRequest) {
        GradeSystemRequestDto gradeSystemToUpdate = gradeSystemMapper.updateRequestToDto(updateGradeSystemRequest);
        GradeSystemDto updatedGradeSystem = gradeSystemService.updateGradeSystem(id, gradeSystemToUpdate);
        return ResponseEntity.ok().body(gradeSystemMapper.dtoToResponse(updatedGradeSystem));
    }

    @Override
    public ResponseEntity<GradeSystemResponse> deleteGradeSystem(Long id) {
        GradeSystemDto deletedGradeSystem = gradeSystemService.deleteGradeSystem(id);
        return ResponseEntity.ok().body(gradeSystemMapper.dtoToResponse(deletedGradeSystem));
    }

    @Override
    public ResponseEntity<List<GradeSystemResponse>> getAllActiveGradeSystems() {
        List<GradeSystemDto> foundActiveGradeSystems = gradeSystemService.getAllActiveGradeSystems();
        return ResponseEntity.ok().body(gradeSystemMapper.listDtoToListResponse(foundActiveGradeSystems));
    }
}
