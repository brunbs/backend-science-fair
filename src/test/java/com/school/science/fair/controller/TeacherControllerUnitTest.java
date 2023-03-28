package com.school.science.fair.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.CreateUserRequest;
import com.school.science.fair.domain.UpdateUserRequest;
import com.school.science.fair.domain.UserResponse;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.*;
import static com.school.science.fair.domain.mother.UsersMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class TeacherControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @MockBean
    private UserServiceImpl userService;

    @SpyBean
    private ObjectMapper mapper;
    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 - POST /teacher - Create a Teacher")
    @Test
    void givenValidCreateUserRequestWhenCreateUserThenReturnUserResponseAnd201Created() throws Exception {
        CreateUserRequest createUserRequest = getCreateUserRequest();

        UserDto createdUserDto = getUserDto();
        createdUserDto.setUserType(UserTypeEnum.TEACHER);
        UserResponse teacherResponse = getUserResponse();
        teacherResponse.setUserType(UserTypeEnum.TEACHER.toString());

        given(userService.createUser(any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(createdUserDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/teacher")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createUserRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        UserResponse returnedTeacher = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);
        assertThat(returnedTeacher).usingRecursiveComparison().isEqualTo(teacherResponse);
        assertThat(createUserRequest.getName()).isEqualTo(returnedTeacher.getName());
        assertThat(createUserRequest.getEmail()).isEqualTo(returnedTeacher.getEmail());
        assertThat(createUserRequest.getRegistration()).isEqualTo(returnedTeacher.getRegistration());
        assertThat(returnedTeacher.getUserType()).isEqualTo(UserTypeEnum.TEACHER.toString());
    }

    @DisplayName("400 - POST /teacher - create teacher with existing email or registration should throw ResourceAlreadyFoundException")
    @Test
    void givenExistingEmailOrRegistrationWhenCreateTeacherThenReturn400BadRequestAndCorrectMessage() throws Exception {

        given(userService.createUser(any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, USER_ALREADY_EXISTS));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/teacher")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(getCreateUserRequest())))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(USER_ALREADY_EXISTS).getMessage());
    }

    @DisplayName("200 - GET /teacher/{teacherRegistration} with valid registration")
    @Test
    void givenValidRegistrationWhenGetTeacherThenReturn200OkAndUserResponse() throws Exception {

        UserDto returnedTeacher = getUserDto();
        returnedTeacher.setUserType(UserTypeEnum.TEACHER);

        given(userService.getUser(anyLong(), any(UserTypeEnum.class))).willReturn(returnedTeacher);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse teacherResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(returnedTeacher.getRegistration()).isEqualTo(teacherResponse.getRegistration());
        assertThat(returnedTeacher.getName()).isEqualTo(teacherResponse.getName());
        assertThat(returnedTeacher.getEmail()).isEqualTo(teacherResponse.getEmail());
        assertThat(returnedTeacher.isActive()).isEqualTo(teacherResponse.getActive());
        assertThat(teacherResponse.getUserType()).isEqualTo(UserTypeEnum.TEACHER.toString());
    }

    @DisplayName("404 - GET/teacher/{teacherRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenGetTeacherThenReturn404NotFound() throws Exception {

        given(userService.getUser(anyLong(), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, TEACHER_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(TEACHER_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - DELETE /teacher/{teacherRegistration} with valid registration")
    @Test
    void givenValidRegistrationWhenDeleteTeacherThenReturn200OkAndUserWithActiveFalse() throws Exception {

        UserDto deletedTeacher = getUserDto();
        deletedTeacher.setActive(false);
        deletedTeacher.setUserType(UserTypeEnum.TEACHER);

        given(userService.deleteUser(anyLong(), any(UserTypeEnum.class))).willReturn(deletedTeacher);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse teacherResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(teacherResponse.getName()).isEqualTo(deletedTeacher.getName());
        assertThat(teacherResponse.getEmail()).isEqualTo(deletedTeacher.getEmail());
        assertThat(teacherResponse.getRegistration()).isEqualTo(deletedTeacher.getRegistration());
        assertThat(teacherResponse.getActive()).isEqualTo(false);
        assertThat(teacherResponse.getUserType()).isEqualTo(UserTypeEnum.TEACHER.toString());
    }

    @DisplayName("404 - DELETE /teacher/{teacherRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenDeleteTeacherThenReturn404NotFound() throws Exception {

        given(userService.deleteUser(anyLong(), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, TEACHER_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(TEACHER_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - PATCH /teacher/{teacherRegistration} with valid Registration and Name")
    @Test
    void givenValidRegistrationAndRequestNameWhenUpdateTeacherThenReturn200OkAndUserResponse() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().name("Teacher B").build();
        UserDto userDto = getUserDto();
        userDto.setUserType(UserTypeEnum.TEACHER);
        userDto.setName(updateUserRequest.getName());

        given(userService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse userResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(userResponse.getName()).isEqualTo(updateUserRequest.getName());
    }

    @DisplayName("200 - PATCH /teacher/{teacherRegistration} with valid Registration and Email")
    @Test
    void givenValidRegistrationAndRequestEmailWhenUpdateTeacherThenReturn200OkAndUserResponse() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().email("newemail@email.com").build();
        UserDto userDto = getUserDto();
        userDto.setUserType(UserTypeEnum.TEACHER);
        userDto.setEmail(updateUserRequest.getEmail());

        given(userService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse teacherResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(teacherResponse.getEmail()).isEqualTo(updateUserRequest.getEmail());
    }

    @DisplayName("404 - PATCH /teacher/{teacherRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenUpdateTeacherThenReturn404NotFound() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().email("newemail@email.com").build();

        given(userService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, TEACHER_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateUserRequest)))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(TEACHER_NOT_FOUND).getMessage());
    }

    @DisplayName("400 - PATCH /teacher/{teacherRegistration} with existing email")
    @Test
    void givenExistingEmailWhenUpdateTeacherThenReturn404NotFound() throws Exception {

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().email("newemail@email.com").build();

        given(userService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/teacher/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateUserRequest)))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(EMAIL_ALREADY_EXISTS).getMessage());
    }

    @DisplayName("200 - GET /teacher/all")
    @Test
    void givenTeachersWhenGetAllTeachersThenReturns200OkAndListOfUserResponseWithUserTypeTeacher() throws Exception {
        List<UserDto> returnedUsers = List.of(
                UserDto.builder().registration(1l).userType(UserTypeEnum.TEACHER).build(),
                UserDto.builder().registration(2l).userType(UserTypeEnum.TEACHER).build(),
                UserDto.builder().registration(3l).userType(UserTypeEnum.TEACHER).build()
        );

        given(userService.getAllUsersByType(UserTypeEnum.TEACHER)).willReturn(returnedUsers);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/all"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<UserResponse> userResponses = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<UserResponse>>() {
        });

        for(UserResponse user : userResponses) {
            assertThat(user.getUserType()).isEqualTo(UserTypeEnum.TEACHER.toString());
        }
        assertThat(userResponses.size()).isEqualTo(returnedUsers.size());
    }

    @DisplayName("200 - GET /teacher/all/active")
    @Test
    void givenTeachersWhenGetAllActiveTeachersThenReturns200OkAndListOfUserResponseWithUserTypeTeacherAndActiveTrue() throws Exception {
        List<UserDto> returnedUsers = List.of(
                UserDto.builder().registration(1l).active(true).userType(UserTypeEnum.TEACHER).build(),
                UserDto.builder().registration(2l).active(true).userType(UserTypeEnum.TEACHER).build(),
                UserDto.builder().registration(3l).active(true).userType(UserTypeEnum.TEACHER).build()
        );

        given(userService.getAllUsersByType(UserTypeEnum.TEACHER)).willReturn(returnedUsers);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/teacher/all"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<UserResponse> userResponses = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<UserResponse>>() {
        });

        for(UserResponse user : userResponses) {
            assertThat(user.getUserType()).isEqualTo(UserTypeEnum.TEACHER.toString());
            assertThat(user.getActive()).isEqualTo(true);
        }
        assertThat(userResponses.size()).isEqualTo(returnedUsers.size());
    }
}
