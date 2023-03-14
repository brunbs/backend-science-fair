package com.school.science.fair.controller;

import com.school.science.fair.api.ClassApi;
import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassController implements ClassApi {

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassMapper classMapper;

    @Override
    public ResponseEntity<ClassResponse> createClass(CreateClassRequest createClassRequest) {
        ClassDto createdClass = classService.createClass(classMapper.requestToDto(createClassRequest));
        ClassResponse classResponse = classMapper.dtoToResponse(createdClass);
        return ResponseEntity.status(HttpStatus.CREATED).body(classResponse);
    }
}
