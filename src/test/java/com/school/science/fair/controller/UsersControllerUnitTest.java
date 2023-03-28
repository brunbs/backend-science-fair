package com.school.science.fair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.CreateUserRequest;
import com.school.science.fair.domain.UpdateStudentRequest;
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

import static com.school.science.fair.domain.enumeration.ExceptionMessage.*;
import static com.school.science.fair.domain.mother.StudentMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class UsersControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @MockBean
    private UserServiceImpl studentService;

    @SpyBean
    private ObjectMapper mapper;
    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 - POST /student - Create a Student")
    @Test
    void givenValidCreateStudentRequestWhenCreateStudentThenReturnStudentDtoAnd201Created() throws Exception {
        CreateUserRequest createStudentRequest = getCreateStudentRequest();

        UserDto createdUserDto = getStudentDto();
        UserResponse studentResponse = getStudentResponse();

        given(studentService.createUser(any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(createdUserDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createStudentRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        UserResponse returnedStudent = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);
        assertThat(returnedStudent).usingRecursiveComparison().isEqualTo(studentResponse);
        assertThat(createStudentRequest.getName()).isEqualTo(returnedStudent.getName());
        assertThat(createStudentRequest.getEmail()).isEqualTo(returnedStudent.getEmail());
        assertThat(createStudentRequest.getRegistration()).isEqualTo(returnedStudent.getRegistration());
    }

    @DisplayName("400 - POST /student - create student with existing email or registration should throw ResourceAlreadyFoundException")
    @Test
    void givenExistingEmailOrRegistrationWhenCreateStudentThenReturn400BadRequestAndCorrectMessage() throws Exception {

        given(studentService.createUser(any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, USER_ALREADY_EXISTS));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(getCreateStudentRequest())))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(USER_ALREADY_EXISTS).getMessage());
    }

    @DisplayName("200 - GET /student/{studentRegistration} with valid registration")
    @Test
    void givenValidRegistrationWhenGetStudentThenReturn200OkAndStudentResponse() throws Exception {

        UserDto returnedStudent = getStudentDto();

        given(studentService.getUser(anyLong(), any(UserTypeEnum.class))).willReturn(returnedStudent);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse studentResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(returnedStudent.getRegistration()).isEqualTo(studentResponse.getRegistration());
        assertThat(returnedStudent.getName()).isEqualTo(studentResponse.getName());
        assertThat(returnedStudent.getEmail()).isEqualTo(studentResponse.getEmail());
        assertThat(returnedStudent.isActive()).isEqualTo(studentResponse.getActive());
    }

    @DisplayName("404 - GET/student/{studentRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenGetStudentThenReturn404NotFound() throws Exception {

        given(studentService.getUser(anyLong(), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, STUDENT_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(STUDENT_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - DELETE /student/{studentRegistration} with valid registration")
    @Test
    void givenValidRegistrationWhenDeleteStudentThenReturn200OkAndStudentWithActiveFalse() throws Exception {

        UserDto deletedStudent = getStudentDto();
        deletedStudent.setActive(false);

        given(studentService.deleteUser(anyLong(), any(UserTypeEnum.class))).willReturn(deletedStudent);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse studentResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(studentResponse.getName()).isEqualTo(deletedStudent.getName());
        assertThat(studentResponse.getEmail()).isEqualTo(deletedStudent.getEmail());
        assertThat(studentResponse.getRegistration()).isEqualTo(deletedStudent.getRegistration());
        assertThat(studentResponse.getActive()).isEqualTo(false);
    }

    @DisplayName("404 - DELETE /student/{studentRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenDeleteStudentThenReturn404NotFound() throws Exception {

        given(studentService.deleteUser(anyLong(), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, STUDENT_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(STUDENT_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - PATCH /student/{studentRegistration} with valid Registration and Name")
    @Test
    void givenValidRegistrationAndRequestNameWhenUpdateStudentThenReturn200OkAndStudentResponse() throws Exception {

        UpdateStudentRequest updateStudentRequest = UpdateStudentRequest.builder().name("Student B").build();
        UserDto userDto = getStudentDto();
        userDto.setName(updateStudentRequest.getName());

        given(studentService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateStudentRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse studentResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(studentResponse.getName()).isEqualTo(updateStudentRequest.getName());
    }

    @DisplayName("200 - PATCH /student/{studentRegistration} with valid Registration and Email")
    @Test
    void givenValidRegistrationAndRequestEmailWhenUpdateStudentThenReturn200OkAndStudentResponse() throws Exception {

        UpdateStudentRequest updateStudentRequest = UpdateStudentRequest.builder().email("newemail@email.com").build();
        UserDto userDto = getStudentDto();
        userDto.setEmail(updateStudentRequest.getEmail());

        given(studentService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willReturn(userDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateStudentRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();

        UserResponse studentResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), UserResponse.class);

        assertThat(studentResponse.getEmail()).isEqualTo(updateStudentRequest.getEmail());
    }

    @DisplayName("404 - PATCH /student/{studentRegistration} with invalid registration")
    @Test
    void givenInvalidRegistrationWhenUpdateStudentThenReturn404NotFound() throws Exception {

        UpdateStudentRequest updateStudentRequest = UpdateStudentRequest.builder().email("newemail@email.com").build();

        given(studentService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, STUDENT_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateStudentRequest)))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(STUDENT_NOT_FOUND).getMessage());
    }

    @DisplayName("400 - PATCH /student/{studentRegistration} with existing email")
    @Test
    void givenExistingEmailWhenUpdateStudentThenReturn404NotFound() throws Exception {

        UpdateStudentRequest updateStudentRequest = UpdateStudentRequest.builder().email("newemail@email.com").build();

        given(studentService.updateUser(anyLong(), any(UserRequestDto.class), any(UserTypeEnum.class))).willThrow(new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/student/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateStudentRequest)))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(EMAIL_ALREADY_EXISTS).getMessage());
    }
}
