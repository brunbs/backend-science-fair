package com.school.science.fair.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.UpdateClassRequest;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.service.impl.ClassServiceImpl;
import org.hamcrest.core.Is;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.CLASS_NOT_FOUND;
import static com.school.science.fair.domain.mother.ClassMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class ClassControllerUnitTest {

    private static final String CLASS_NOT_FOUND_MESSAGE = "Turma não encontrada";

    @Autowired
    MockMvc mockMvc;

    @Spy
    private ClassMapper classMapper = Mappers.getMapper(ClassMapper.class);

    @MockBean
    private ClassServiceImpl classService;

    @SpyBean
    private ObjectMapper mapper;
    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 - POST /class - Create a Class")
    @Test
    void givenValidCreateClassRequestWhenCreateClassThenReturnClassDtoAnd201Created() throws Exception {

        CreateClassRequest createClassRequest = getCreateClassRequest();
        ClassDto createdClassDto = getClassDto();
        ClassResponse classResponse = getClassResponse();

        given(classService.createClass(any(ClassRequestDto.class))).willReturn(createdClassDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/class")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createClassRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        ClassResponse returnedClass = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ClassResponse.class);
        assertThat(returnedClass).usingRecursiveComparison().isEqualTo(classResponse);
        assertThat(createClassRequest.getName()).isEqualTo(returnedClass.getName());
    }

    @DisplayName("400 - POST /class - Create a Class with no name or grade year")
    @Test
    void givenInvalidCreateClassRequestWhenCreateClassThenReturn404BadRequest() throws Exception {

        CreateClassRequest createClassRequest = getCreateClassRequest();
        createClassRequest.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/class")
                        .content(new ObjectMapper().writeValueAsString(createClassRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

        createClassRequest.setName("Class A");
        createClassRequest.setGradeYear(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/class")
                        .content(new ObjectMapper().writeValueAsString(createClassRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeYear", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

    }

    @DisplayName("200 - GET /class/{classId} with valid classId")
    @Test
    void givenValidClassIdWhenGetClassThenReturn200OkAndClassResponse() throws Exception{

        ClassDto returnedClassDto = getClassDto();
        given(classService.getClass(anyLong())).willReturn(returnedClassDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/class/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse();

        ClassResponse classResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ClassResponse.class);

        assertThat(returnedClassDto.getId()).isEqualTo(classResponse.getId());
        assertThat(returnedClassDto.getName()).isEqualTo(classResponse.getName());
        assertThat(returnedClassDto.getGradeYear().getDescription()).isEqualTo(classResponse.getGradeYear());
    }

    @DisplayName("404 - GET /class/{classId} with invalid classId")
    @Test
    void givenInvalidClassIdWhenGetClassThenReturn404NotFoundWithCorrectMessage() throws Exception {
        given(classService.getClass(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, CLASS_NOT_FOUND));
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/class/1")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(CLASS_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - GET /class/classes/active")
    @Test
    void givenListOfClassesWhenGetActiveClassesThenReturn200AndAListOfActiveClasses() throws Exception {
        ClassDto firstActiveClass = getClassDto();
        ClassDto secondActiveClass = getClassDto();
        secondActiveClass.setId(2l);
        ClassDto thirdActiveClass = getClassDto();
        thirdActiveClass.setId(3l);
        List<ClassDto> returnedClasses = Arrays.asList(firstActiveClass, secondActiveClass, thirdActiveClass);

        given(classService.getAllActiveClasses()).willReturn(returnedClasses);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/class/classes/active")).andExpect(status().isOk()).andReturn().getResponse();
        List<ClassResponse> classResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ClassResponse>>(){});

        assertThat(classResponse.size()).isEqualTo(returnedClasses.size());

    }

    @DisplayName("200 - DELETE /class/{classId} should delete and return deleted class")
    @Test
    void givenValidClassIdWhenDeleteClassThenReturn200AndTheDeletedClass() throws Exception {

        ClassDto deletedClass = getClassDto();
        deletedClass.setActive(false);
        given(classService.deleteClass(anyLong())).willReturn(deletedClass);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/class/1")).andExpect(status().isOk()).andReturn().getResponse();
        ClassResponse classResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ClassResponse.class);

        assertThat(classResponse.getId()).isEqualTo(deletedClass.getId());
        assertThat(classResponse.getName()).isEqualTo(deletedClass.getName());
        assertThat(classResponse.getGradeYear()).isEqualTo(deletedClass.getGradeYear().getDescription());
        assertThat(deletedClass.isActive()).isFalse();
    }

    @DisplayName("200 - PATCH /class/{classId} should update and return updated class")
    @Test
    void givenValidClassIdAndClassUpdateRequestWhenUpdateClassThenReturn200AndUpdatedClass() throws Exception {

        UpdateClassRequest updateClassRequest = getUpdateClassRequest();
        ClassDto updatedClassDto = getClassDto();
        updatedClassDto.setName(updateClassRequest.getName());
        given(classService.updateClass(anyLong(), any(ClassRequestDto.class))).willReturn(updatedClassDto);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/class/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(updateClassRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();
        ClassResponse classResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ClassResponse.class);

        assertThat(updateClassRequest.getName()).isEqualTo(classResponse.getName());
    }

}
