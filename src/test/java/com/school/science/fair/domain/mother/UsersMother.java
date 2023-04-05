package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateUserRequest;
import com.school.science.fair.domain.UserResponse;
import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.UserTypeEnum;

public class UsersMother {

    public static Users getStudentEntity() {
        return Users.builder()
                .registration(1l)
                .name("Student A")
                .email("student@email.com")
                .active(true)
                .password("1")
                .userType(UserTypeEnum.STUDENT)
                .build();
    }

    public static UserRequestDto getUserRequestDto() {
        return UserRequestDto.builder()
                .name("Student A")
                .email("student@email.com")
                .registration(1l)
                .build();
    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .registration(1l)
                .email("student@email.com")
                .name("Student A")
                .password("1")
                .active(true)
                .userType(UserTypeEnum.STUDENT)
                .build();
    }

    public static CreateUserRequest getCreateUserRequest() {
        return CreateUserRequest.builder()
                .registration(1l)
                .name("Student A")
                .email("student@email.com")
                .build();
    }

    public static UserResponse getUserResponse() {
        return UserResponse.builder()
                .active(true)
                .email("student@email.com")
                .registration(1l)
                .name("Student A")
                .userType(UserTypeEnum.STUDENT.toString())
                .build();
    }

    public static Users getTeacherEntity() {
        return Users.builder()
                .registration(3l)
                .name("Teacher")
                .email("teaher@email.com")
                .active(true)
                .password("1")
                .userType(UserTypeEnum.TEACHER)
                .build();
    }

}
