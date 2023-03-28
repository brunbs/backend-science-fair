package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.repository.UserRepository;
import com.school.science.fair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto createUser(UserRequestDto createUserDto, UserTypeEnum userType) {
        findIfUserAlreadyExistsByRegistrationOrEmail(createUserDto.getEmail(), createUserDto.getRegistration());
        Users usersToCreate = userMapper.createDtoToEntity(createUserDto);
        usersToCreate.setUserType(userType);
        usersToCreate.setPassword(usersToCreate.getRegistration().toString());
        usersToCreate.setActive(true);
        Users createdUsers = userRepository.save(usersToCreate);
        return userMapper.entityToDto(createdUsers);
    }

    @Override
    public UserDto getUser(Long userRegistration, UserTypeEnum userType) {
        Users foundUser = findUserByRegistrationAndTypeOrThrowException(userRegistration, userType);
        return userMapper.entityToDto(foundUser);
    }

    @Override
    public UserDto deleteUser(Long studentRegistration, UserTypeEnum userType) {
        Users foundUsers = findUserByRegistrationAndTypeOrThrowException(studentRegistration, userType);
        foundUsers.setActive(false);
        Users deletedUsers = userRepository.save(foundUsers);
        return userMapper.entityToDto(deletedUsers);
    }

    @Override
    public UserDto updateUser(Long userRegistration, UserRequestDto updateUserDto, UserTypeEnum userType) {
        Users foundUsers = findUserByRegistrationAndTypeOrThrowException(userRegistration, userType);
        if(updateUserDto.getEmail() != null) {
            findUserByEmailOrThrowException(updateUserDto.getEmail());
        }
        userMapper.updateModelFromDto(updateUserDto, foundUsers);
        Users updatedUsers = userRepository.save(foundUsers);
        return userMapper.entityToDto(updatedUsers);
    }

    @Override
    public List<UserDto> getAllUsersByType(UserTypeEnum userTypeEnum) {
        List<Users> foundUsers = userRepository.findAllByUserType(userTypeEnum);
        return userMapper.listEntityToListDto(foundUsers);
    }

    @Override
    public List<UserDto> getAllActiveUsersByType(UserTypeEnum userTypeEnum) {
        List<Users> foundUsers = userRepository.findAllByActiveTrueAndUserType(userTypeEnum);
        return userMapper.listEntityToListDto(foundUsers);
    }

    private Users findUserByRegistrationAndTypeOrThrowException(Long id, UserTypeEnum userTypeEnum) {
        Optional<Users> foundUserEntity = userRepository.findByRegistrationAndUserType(id, userTypeEnum);
        if(foundUserEntity.isEmpty()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, UserTypeEnum.getNotFoundMessage(userTypeEnum));
        }
        return foundUserEntity.get();
    }

    private void findIfUserAlreadyExistsByRegistrationOrEmail(String email, Long registration) {
        Optional<Users> foundStudentEntity = userRepository.findByEmailOrRegistration(email, registration);
        if(foundStudentEntity.isPresent()) {
            throw new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, ExceptionMessage.USER_ALREADY_EXISTS);
        }
    }

    private void findUserByEmailOrThrowException(String email) {
        Optional<Users> foundUserEntity = userRepository.findByEmail(email);
        if (foundUserEntity.isPresent()) {
            throw new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, ExceptionMessage.EMAIL_ALREADY_EXISTS);
        }
    }

}
