package com.school.science.fair.controller;

import com.school.science.fair.api.TeacherApi;
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
public class TeacherController implements TeacherApi {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<UserResponse> createTeacher(CreateUserRequest createUserRequest) {
        UserRequestDto teacherToCreate = userMapper.createRequestToDto(createUserRequest);
        UserDto createdTeacher = userService.createUser(teacherToCreate, UserTypeEnum.TEACHER);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.dtoToResponse(createdTeacher));
    }

    @Override
    public ResponseEntity<UserResponse> getTeacher(Long teacherRegistration) {
        UserDto foundTeacher = userService.getUser(teacherRegistration, UserTypeEnum.TEACHER);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(foundTeacher));
    }

    @Override
    public ResponseEntity<UserResponse> updateTeacher(Long teacherRegistration, UpdateUserRequest updateUserRequest) {
        UserRequestDto teacherInfoToUpdate = userMapper.updateToDto(updateUserRequest);
        UserDto updatedTeacher = userService.updateUser(teacherRegistration, teacherInfoToUpdate, UserTypeEnum.TEACHER);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(updatedTeacher));
    }

    @Override
    public ResponseEntity<UserResponse> deleteTeacher(Long teacherRegistration) {
        UserDto deletedTeacher = userService.deleteUser(teacherRegistration, UserTypeEnum.TEACHER);
        return ResponseEntity.ok().body(userMapper.dtoToResponse(deletedTeacher));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllActiveTeachers() {
        List<UserDto> foundTeachers = userService.getAllActiveUsersByType(UserTypeEnum.TEACHER);
        return ResponseEntity.ok().body(userMapper.listDtoToListResponse(foundTeachers));
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllTeachers() {
        List<UserDto> foundTeachers = userService.getAllUsersByType(UserTypeEnum.TEACHER);
        return ResponseEntity.ok().body(userMapper.listDtoToListResponse(foundTeachers));
    }
}
