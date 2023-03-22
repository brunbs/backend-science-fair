package com.school.science.fair.controller;

import com.school.science.fair.api.GradeSystemApi;
import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.GradeSystemResponse;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.service.GradeSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
