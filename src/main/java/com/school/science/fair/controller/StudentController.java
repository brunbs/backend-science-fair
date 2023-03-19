package com.school.science.fair.controller;

import com.school.science.fair.api.StudentApi;
import com.school.science.fair.domain.CreateStudentRequest;
import com.school.science.fair.domain.StudentResponse;
import com.school.science.fair.domain.UpdateStudentRequest;
import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.mapper.StudentMapper;
import com.school.science.fair.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController implements StudentApi {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public ResponseEntity<StudentResponse> createStudent(CreateStudentRequest createStudentRequest) {
        StudentRequestDto studentRequestDto = studentMapper.createRequestToDto(createStudentRequest);
        StudentDto createdStudent = studentService.createStudent(studentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentMapper.dtoToResponse(createdStudent));
    }

    @Override
    public ResponseEntity<StudentResponse> getStudent(Long studentRegistration) {
        StudentDto foundStudent = studentService.getStudent(studentRegistration);
        return ResponseEntity.ok().body(studentMapper.dtoToResponse(foundStudent));
    }

    @Override
    public ResponseEntity<StudentResponse> deleteStudent(Long studentRegistration) {
        StudentDto deletedStudent = studentService.deleteStudent(studentRegistration);
        return ResponseEntity.ok().body(studentMapper.dtoToResponse(deletedStudent));
    }

    @Override
    public ResponseEntity<StudentResponse> updateStudent(Long studentRegistration, UpdateStudentRequest updateStudentRequest) {
        StudentRequestDto studentRequestDto = studentMapper.updateToDto(updateStudentRequest);
        StudentDto updatedStudent = studentService.updateStudent(studentRegistration, studentRequestDto);
        return ResponseEntity.ok().body(studentMapper.dtoToResponse(updatedStudent));
    }
}
