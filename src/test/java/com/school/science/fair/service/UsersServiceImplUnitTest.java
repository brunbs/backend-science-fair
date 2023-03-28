package com.school.science.fair.service;

import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.repository.UserRepository;
import com.school.science.fair.service.impl.UserServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.*;
import static com.school.science.fair.domain.mother.UsersMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class UsersServiceImplUnitTest {

    @SpyBean
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Database save student")
    @Test
    void givenUserEntityWhenSaveUserThenReturnSavedEntityWithRightDetails() {
        Users usersEntity = getStudentEntity();

        Users storedUsersEntity = testEntityManager.persistAndFlush(usersEntity);

        assertThat(usersEntity.getName()).isEqualTo(storedUsersEntity.getName());
        assertThat(usersEntity.getEmail()).isEqualTo(storedUsersEntity.getEmail());
        assertThat(usersEntity.getPassword()).isEqualTo(storedUsersEntity.getPassword());
        assertThat(usersEntity.isActive()).isEqualTo(storedUsersEntity.isActive());
    }

    @DisplayName("Create a student")
    @Test
    void givenValidUserRequestDtoWhenCreateUserThenCreatesStudentAndReturnStudentDto() {
        UserRequestDto userRequestDto = getUserRequestDto();
        Users usersEntity = getStudentEntity();
        Users createdUsers = getStudentEntity();
        UserDto createdUserDto = getUserDto();

        given(userRepository.findByEmailOrRegistration(anyString(), anyLong())).willReturn(Optional.empty());
        given(userMapper.createDtoToEntity(userRequestDto)).willReturn(usersEntity);
        given(userRepository.save(usersEntity)).willReturn(createdUsers);
        given(userMapper.entityToDto(createdUsers)).willReturn(createdUserDto);

        UserDto returnedStudent = userService.createUser(userRequestDto, UserTypeEnum.STUDENT);

        assertThat(returnedStudent).usingRecursiveComparison().isEqualTo(createdUserDto);
        assertThat(returnedStudent.getUserType()).isEqualTo(UserTypeEnum.STUDENT);

        verify(userRepository).findByEmailOrRegistration(anyString(), anyLong());
        verify(userRepository).save(any(Users.class));

    }

    @DisplayName("Create Student when registration or email already exists Should Throw ResourceAlreadyExistsException")
    @Test
    void givenUserRequestDtoWithEmailOrRegistrationAlreadyInUseWhenCreateUserThenThrowsResourceAlreadyExistsException() {
        UserRequestDto userRequestDto = getUserRequestDto();

        given(userRepository.findByEmailOrRegistration(anyString(), anyLong())).willReturn(Optional.of(getStudentEntity()));

        assertThatThrownBy(
                () -> userService.createUser(userRequestDto, UserTypeEnum.STUDENT)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(USER_ALREADY_EXISTS.getMessageKey());
    }

    @DisplayName("Get a student user")
    @Test
    void givenValidRegistrationWhenGetStudentUserThenReturnsUserDto() {
        Users usersEntity = getStudentEntity();

        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.of(usersEntity));

        UserDto returnedStudent = userService.getUser(1l, UserTypeEnum.STUDENT);

        assertThat(returnedStudent).usingRecursiveComparison().isEqualTo(usersEntity);
    }

    @DisplayName("Get a not existent student user should throw ResourceNotFoundException")
    @Test
    void givenInvalidRegistrationWhenGetStudentThenThrowResourceNotFoundExceptionWithCorrectMessage() {

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> userService.getUser(1l, UserTypeEnum.STUDENT)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(STUDENT_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get a not existent teacher user should throw ResourceNotFoundException")
    @Test
    void givenInvalidRegistrationWhenGetTeacherUserThenThrowResourceNotFoundExceptionWithCorrectMessage() {

        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> userService.getUser(1l, UserTypeEnum.TEACHER)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(TEACHER_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Delete user should change active to false")
    @Test
    void givenValidUserRegistrationWhenDeleteUserThenChangeEntityActiveToFalseAndReturnUserDto() {
        Users foundUsers = getStudentEntity();

        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.of(foundUsers));

        UserDto deletedUserDto = userService.deleteUser(1l, UserTypeEnum.STUDENT);

        assertThat(deletedUserDto.isActive()).isFalse();

    }

    @DisplayName("Update user")
    @Test
    void givenValidNameUserRequestDtoWhenUpdateUserThenReturnStudentDto() {
        Users foundUsers = getStudentEntity();
        UserRequestDto updateInfo = UserRequestDto.builder().name("Student B").build();

        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.of(foundUsers));

        UserDto updatedStudent = userService.updateUser(1l, updateInfo, UserTypeEnum.STUDENT);

        assertThat(updatedStudent.getName()).isEqualTo(updateInfo.getName());
        verify(userRepository).save(foundUsers);

    }

    @DisplayName("Update user")
    @Test
    void givenValidEmailUserRequestDtoWhenUpdateUserThenReturnStudentDto() {
        Users foundUsers = getStudentEntity();
        UserRequestDto updateInfo = UserRequestDto.builder().email("newemail@email.com").build();

        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.of(foundUsers));
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        UserDto updatedStudent = userService.updateUser(1l, updateInfo, UserTypeEnum.STUDENT);

        assertThat(updatedStudent.getEmail()).isEqualTo(updateInfo.getEmail());
        verify(userRepository).save(foundUsers);

    }

    @DisplayName("Update user with email already registered")
    @Test
    void givenEmailAlreadyRegisteredWhenUpdateUserThenThrowsResourceAlreadyExistsExceptionWithCorrectMessage() {
        Users foundUsers = getStudentEntity();

        UserRequestDto updateInfo = UserRequestDto.builder().email("newemail@email.com").build();
        given(userRepository.findByRegistrationAndUserType(anyLong(), any(UserTypeEnum.class))).willReturn(Optional.of(foundUsers));
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(foundUsers));

        assertThatThrownBy(
                () -> userService.updateUser(1l, updateInfo, UserTypeEnum.STUDENT)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(EMAIL_ALREADY_EXISTS.getMessageKey());
    }

    @DisplayName("Get all users by type returns all users with given type")
    @Test
    void givenUserTypeWhenGetAllUsersByTypeThenReturnsUserDtoListOfWithGivenTypeOnly() {
        testEntityManager.persistAndFlush(Users.builder().registration(1l).active(true).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(2l).active(false).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(3l).active(true).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(4l).active(false).userType(UserTypeEnum.TEACHER).build());
        testEntityManager.persistAndFlush(Users.builder().registration(5l).active(true).userType(UserTypeEnum.TEACHER).build());
        testEntityManager.persistAndFlush(Users.builder().registration(6l).active(true).userType(UserTypeEnum.TEACHER).build());

        List<UserDto> returnedUsers = userService.getAllUsersByType(UserTypeEnum.STUDENT);

        for(UserDto user : returnedUsers) {
            assertThat(user.getUserType()).isEqualTo(UserTypeEnum.STUDENT);
        }
    }

    @DisplayName("Get all active users by type returns all users with given type and active true")
    @Test
    void givenUserTypeWhenGetAllActiveUsersByTypeThenReturnsUserDtoListOfWithGivenTypeOnlyAndActiveTrue() {
        testEntityManager.persistAndFlush(Users.builder().registration(1l).active(true).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(2l).active(false).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(3l).active(true).userType(UserTypeEnum.STUDENT).build());
        testEntityManager.persistAndFlush(Users.builder().registration(4l).active(false).userType(UserTypeEnum.TEACHER).build());
        testEntityManager.persistAndFlush(Users.builder().registration(5l).active(true).userType(UserTypeEnum.TEACHER).build());
        testEntityManager.persistAndFlush(Users.builder().registration(6l).active(true).userType(UserTypeEnum.TEACHER).build());

        List<UserDto> returnedUsers = userService.getAllActiveUsersByType(UserTypeEnum.STUDENT);

        for(UserDto user : returnedUsers) {
            assertThat(user.getUserType()).isEqualTo(UserTypeEnum.STUDENT);
            assertThat(user.isActive()).isTrue();
        }
    }

}
