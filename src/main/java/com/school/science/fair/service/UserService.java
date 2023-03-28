package com.school.science.fair.service;

import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.enumeration.UserTypeEnum;

import java.util.List;

public interface UserService {

    UserDto createUser(UserRequestDto createUserDto, UserTypeEnum userType);
    UserDto getUser(Long userRegistration, UserTypeEnum userType);
    UserDto deleteUser(Long userRegistration, UserTypeEnum userType);
    UserDto updateUser(Long userRegistration, UserRequestDto updateUserDto, UserTypeEnum userTypeEnum);
    List<UserDto> getAllUsersByType(UserTypeEnum userTypeEnum);
    List<UserDto> getAllActiveUsersByType(UserTypeEnum userTypeEnum);

}
