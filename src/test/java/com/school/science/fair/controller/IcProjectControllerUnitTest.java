package com.school.science.fair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.CreateProjectRequest;
import com.school.science.fair.domain.ProjectResponse;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.service.impl.IcProjectServiceImpl;
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

import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_SYSTEM_NOT_FOUND;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.PROJECT_NOT_FOUND;
import static com.school.science.fair.domain.mother.IcProjectMother.getCreateProjectRequest;
import static com.school.science.fair.domain.mother.IcProjectMother.getProjectDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class IcProjectControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IcProjectServiceImpl icProjectService;

    @Spy
    private IcProjectMapper projectMapper = Mappers.getMapper(IcProjectMapper.class);

    @SpyBean
    private ObjectMapper mapper;

    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 - /project/science-fair/{scienceFairId} with valid Science Fair Id")
    @Test
    void givenValidScienceFairIdAndCreateProjectRequestWhenCreateProjectThenReturns201CreatedAndProjectResponse() throws Exception {
        CreateProjectRequest createProjectRequest = getCreateProjectRequest();
        ProjectDto projectDto = getProjectDto();

        given(icProjectService.createProject(anyLong(), any(CreateProjectDto.class))).willReturn(projectDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/project/science-fair/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createProjectRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        ProjectResponse returnedProject = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ProjectResponse.class);

        assertThat(returnedProject.getId()).isEqualTo(projectDto.getId());
        assertThat(returnedProject.getTitle()).isEqualTo(createProjectRequest.getTitle());
        assertThat(returnedProject.getDescription()).isEqualTo(createProjectRequest.getDescription());
        assertThat(returnedProject.getTopic()).usingRecursiveComparison().isEqualTo(projectDto.getTopic());
        assertThat(returnedProject.getStudents()).usingRecursiveComparison().isEqualTo(projectDto.getStudents());
        assertThat(returnedProject.getTeacher()).usingRecursiveComparison().isEqualTo(projectDto.getTeacher());
    }

    @DisplayName("200 - GET /project/{projectId} - get project details")
    @Test
    void givenValidProjectIdWhenGetProjectThenReturns200OkAndProjectResponse() throws Exception {

        ProjectDto projectDto = getProjectDto();

        given(icProjectService.getProject(anyLong())).willReturn(projectDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/project/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        ProjectResponse returnedProject = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ProjectResponse.class);

        assertThat(returnedProject).usingRecursiveComparison().ignoringFields("gradeSum").isEqualTo(projectDto);
    }

    @DisplayName("404 - GET /project/{projectId} get project details with invalid id")
    @Test
    void givenInvalidProjectIdWhenGetProjectThenReturns404NotFound() throws Exception {

        given(icProjectService.getProject(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/project/1")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(PROJECT_NOT_FOUND).getMessage());

    }

}
