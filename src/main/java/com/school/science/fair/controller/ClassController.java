package com.school.science.fair.controller;

import com.school.science.fair.api.ClassApi;
import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.UpdateClassRequest;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassController implements ClassApi {

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassMapper classMapper;

    @Override
    public ResponseEntity<ClassResponse> createClass(CreateClassRequest createClassRequest) {
        ClassDto createdClass = classService.createClass(classMapper.toRequestDto(createClassRequest));
        ClassResponse classResponse = classMapper.dtoToResponse(createdClass);
        return ResponseEntity.status(HttpStatus.CREATED).body(classResponse);
    }

    @Override
    public ResponseEntity<ClassResponse> getClass(Long classId) {
        ClassDto foundClass = classService.getClass(classId);
        return ResponseEntity.ok(classMapper.dtoToResponse(foundClass));
    }

    @Override
    public ResponseEntity<List<ClassResponse>> getAllActiveClasses() {
        List<ClassDto> classes = classService.getAllActiveClasses();
        return ResponseEntity.ok(classMapper.toClassResponseList(classes));
    }

    @Override
    public ResponseEntity<ClassResponse> deleteClass(Long classId) {
        ClassDto deletedClass = classService.deleteClass(classId);
        return ResponseEntity.ok(classMapper.dtoToResponse(deletedClass));
    }

    @Override
    public ResponseEntity<ClassResponse> updateClass(Long classId, UpdateClassRequest updateClassRequest) {
        ClassRequestDto classToUpdate = classMapper.toRequestDto(updateClassRequest);
        ClassDto updatedClass = classService.updateClass(classId, classToUpdate);
        return ResponseEntity.ok(classMapper.dtoToResponse(updatedClass));
    }
}
