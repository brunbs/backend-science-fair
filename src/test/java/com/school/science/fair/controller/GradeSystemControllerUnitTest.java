package com.school.science.fair.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.GradeResponse;
import com.school.science.fair.domain.GradeSystemResponse;
import com.school.science.fair.domain.UpdateGradeSystemRequest;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.service.impl.GradeSystemServiceImpl;
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

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_SYSTEM_NOT_FOUND;
import static com.school.science.fair.domain.mother.GradeSystemMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class GradeSystemControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    private ObjectMapper mapper;

    @MockBean
    private GradeSystemServiceImpl gradeSystemService;

    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @Spy
    private GradeSystemMapper gradeSystemMapper = Mappers.getMapper(GradeSystemMapper.class);

    @DisplayName("201 - POST /grade-system")
    @Test
    void givenCreateGradeSystemRequestWhenCreateGradeSystemReturns201CreatedAndGradeSystemResponse() throws Exception {

        CreateGradeSystemRequest createGradeSystemRequest = getCreateGradeSystemRequest();
        GradeSystemDto gradeSystemDto = getGradeSystemDto();

        given(gradeSystemService.createGradeSystem(any(GradeSystemRequestDto.class))).willReturn(gradeSystemDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/grade-system")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createGradeSystemRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        GradeSystemResponse returnedGradeSystem = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), GradeSystemResponse.class);

        assertThat(returnedGradeSystem.getActive()).isTrue();
        assertThat(returnedGradeSystem).usingRecursiveComparison().ignoringFields("active", "id", "grades.id").isEqualTo(createGradeSystemRequest);
        for (GradeResponse grade : returnedGradeSystem.getGrades()) {
            assertThat(grade.getId()).isNotNull();
        }
    }

    @DisplayName("400 - POST /grade-system with no name")
    @Test
    void givenCreateGradeSystemRequestWithNoNameWhenCreateGradeSystemThenReturns400BadRequest() throws Exception {
        CreateGradeSystemRequest createGradeSystemRequest = getCreateGradeSystemRequest();
        createGradeSystemRequest.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/grade-system")
                        .content(new ObjectMapper().writeValueAsString(createGradeSystemRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("200 - GET /grade-system/{id} with valid id")
    @Test
    void givenValidIdWhenGetGradeSystemThenReturns200OkAndGradeSystemResponse() throws Exception {
        GradeSystemDto gradeSystemDto = getGradeSystemDto();

        given(gradeSystemService.getGradeSystem(anyLong())).willReturn(gradeSystemDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/grade-system/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        GradeSystemResponse returnedGradeSystem = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), GradeSystemResponse.class);

        assertThat(returnedGradeSystem).usingRecursiveComparison().isEqualTo(gradeSystemDto);
    }

    @DisplayName("404 - GET /grade-system/{id} with invalid id")
    @Test
    void givenInvalidIdWhenGetGradeSystemThenReturns404NotFound() throws Exception {

        given(gradeSystemService.getGradeSystem(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, GRADE_SYSTEM_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/grade-system/1")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(GRADE_SYSTEM_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - GET /grade-system/all")
    @Test
    void givenGradeSystemsWhenGetGradeSystemThenReturns200OkAndListOfGradeSystemResponse() throws Exception {

        List<GradeSystemDto> gradeSystemDtos = getGradeSystemDtos();

        given(gradeSystemService.getAllGradeSystem()).willReturn(gradeSystemDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/grade-system/all"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<GradeSystemResponse> gradeSystemResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<GradeSystemResponse>>() {
        });

        assertThat(gradeSystemResponse).usingRecursiveComparison().isEqualTo(gradeSystemDtos);
    }

    @DisplayName("200 - PATCH /grade-system/{id} with valid Id")
    @Test
    void givenValidIdAndUpdateGradeSystemRequestWhenUpdateGradeSystemThenReturns200OkAndGradeSystemDto() throws Exception {
        GradeSystemDto updatedGradeSystem = getGradeSystemDto();
        UpdateGradeSystemRequest updateGradeSystemRequest = UpdateGradeSystemRequest.builder().name("New Name").build();
        updatedGradeSystem.setName(updateGradeSystemRequest.getName());

        given(gradeSystemService.updateGradeSystem(anyLong(), any(GradeSystemRequestDto.class))).willReturn(updatedGradeSystem);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/grade-system/1")
                .content(new ObjectMapper().writeValueAsString(updateGradeSystemRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse();

        GradeSystemResponse returnedGradeSystemResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), GradeSystemResponse.class);
        assertThat(returnedGradeSystemResponse.getName()).isEqualTo(updateGradeSystemRequest.getName());
    }

    @DisplayName("404 PATCH - /grade-system/{id} with invalid id")
    @Test
    void givenInvalidIdWhenUpdateGradeSystemThenReturns404NotFound() throws Exception {
        UpdateGradeSystemRequest updateGradeSystemRequest = UpdateGradeSystemRequest.builder().name("New Name").build();

        given(gradeSystemService.updateGradeSystem(anyLong(), any(GradeSystemRequestDto.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, GRADE_SYSTEM_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/grade-system/1")
                .content(new ObjectMapper().writeValueAsString(updateGradeSystemRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(GRADE_SYSTEM_NOT_FOUND).getMessage());

    }

    @DisplayName("200 GET - /grade-system/all/active")
    @Test
    void givenActiveGradeSystemsWhenGetGradeSystemThenReturns200OkAndListOfActiveGradeSystemResponse() throws Exception {
        List<GradeSystemDto> activeTrueDtos = getGradeSystemDtos().stream().filter(GradeSystemDto::isActive).collect(Collectors.toList());

        given(gradeSystemService.getAllActiveGradeSystems()).willReturn(activeTrueDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/grade-system/all/active"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<GradeSystemResponse> gradeSystemResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<GradeSystemResponse>>() {
        });

        for(GradeSystemResponse gradeSystem : gradeSystemResponse) {
            assertThat(gradeSystem.getActive()).isTrue();
        }
    }

    @DisplayName("200 - DELETE - /grade-system/{id} with valid id")
    @Test
    void givenValidIdWhenDeleteGradeSystemThenReturns200OkAndGradeSystemDtoWithActiveFalse() throws Exception {
        GradeSystemDto deletedGradeSystem = getGradeSystemDto();
        deletedGradeSystem.setActive(false);

        given(gradeSystemService.deleteGradeSystem(anyLong())).willReturn(deletedGradeSystem);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/grade-system/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        GradeSystemResponse returnedGradeSystemResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), GradeSystemResponse.class);

        assertThat(returnedGradeSystemResponse.getActive()).isFalse();
    }

    @DisplayName("404 - DELETE /grade-system/{id} with invalid id")
    @Test
    void givenInvalidIdWhenDeleteGradeSystemThenReturns404NotFound() throws Exception {

        given(gradeSystemService.deleteGradeSystem(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, GRADE_SYSTEM_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/grade-system/1")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(GRADE_SYSTEM_NOT_FOUND).getMessage());
    }

}
