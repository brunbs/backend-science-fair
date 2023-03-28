package com.school.science.fair.controller;

import com.school.science.fair.api.StudentApi;
import com.school.science.fair.domain.CreateUserRequest;
import com.school.science.fair.domain.UpdateUserRequest;
import com.school.science.fair.domain.UserResponse;
import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController implements StudentApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<UserResponse> createStudent(CreateUserRequest createStudentRequest) {
        UserRequestDto userRequestDto = userMapper.createRequestToDto(createStudentRequest);
        UserDto createdStudent = userService.createUser(userRequestDto, UserTypeEnum.STUDENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.dtoToResponse(createdStudent));
    }

    @Override
    public ResponseEntity<UserResponse> getStudent(Long studentRegistration) {
        UserDto foundStudent = userService.getUser(studentRegistration, UserTypeEnum.STUDENT);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(foundStudent));
    }

    @Override
    public ResponseEntity<UserResponse> deleteStudent(Long studentRegistration) {
        UserDto deletedStudent = userService.deleteUser(studentRegistration, UserTypeEnum.STUDENT);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(deletedStudent));
    }

    @Override
    public ResponseEntity<UserResponse> updateStudent(Long studentRegistration, UpdateUserRequest updateStudentRequest) {
        UserRequestDto userRequestDto = userMapper.updateToDto(updateStudentRequest);
        UserDto updatedStudent = userService.updateUser(studentRegistration, userRequestDto, UserTypeEnum.STUDENT);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(updatedStudent));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllActiveStudents() {
        List<UserDto> foundStudents = userService.getAllActiveUsersByType(UserTypeEnum.STUDENT);
        return ResponseEntity.ok().body(userMapper.listDtoToListResponse(foundStudents));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllStudents() {
        List<UserDto> foundStudents = userService.getAllUsersByType(UserTypeEnum.STUDENT);
        return ResponseEntity.ok().body(userMapper.listDtoToListResponse(foundStudents));
    }
}
