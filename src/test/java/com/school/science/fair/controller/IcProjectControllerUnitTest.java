package com.school.science.fair.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateProjectRequest;
import com.school.science.fair.domain.ProjectResponse;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.PROJECT_NOT_FOUND;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.SCIENCE_FAIR_NOT_FOUND;
import static com.school.science.fair.domain.mother.IcProjectMother.getCreateProjectRequest;
import static com.school.science.fair.domain.mother.IcProjectMother.getProjectDto;
import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("200 - GET /project/science-fair/{scienceFairId}/projects get all projects from a science fair")
    @Test
    void givenValidScienceFairIdWhenGetAllProjectsFromScienceFairThenReturns200OkAndProjectResponseList() throws Exception {
        ProjectDto projectADto = getProjectDto();
        ProjectDto projectBDto = getProjectDto();
        projectBDto.setTitle("Project B");
        projectBDto.setDescription("Project B Description");
        List<ProjectDto> projectDtos = List.of(projectADto, projectBDto);

        given(icProjectService.getAllProjectsFromScienceFair(anyLong())).willReturn(projectDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/project/science-fair/1/projects"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<ProjectResponse> returnedProjects = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProjectResponse>>() {});
        assertThat(returnedProjects).usingRecursiveComparison().isEqualTo(projectDtos);
    }

    @DisplayName("404 - GET /project/science-fair/{scienceFairId}/projects with invalid scienceFairId")
    @Test
    void givenInvalidScienceFairIdWhenGetAllProjectsFromScienceFairThenReturns404NotFound() throws Exception {
        given(icProjectService.getAllProjectsFromScienceFair(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, SCIENCE_FAIR_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/project/science-fair/1/projects"))
                .andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(SCIENCE_FAIR_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - DELETE /project/{projectId} with valid projectId")
    @Test
    void givenValidProjectIdWhenDeleteProjectThenReturns200OkAndDeletedProject() throws Exception {
        ProjectDto projectDto = getProjectDto();
        given(icProjectService.deleteProject(anyLong())).willReturn(projectDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/project/1")).andExpect(status().isOk()).andReturn().getResponse();
        ProjectResponse projectResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ProjectResponse.class);

        assertThat(projectResponse).usingRecursiveComparison().isEqualTo(projectDto);
    }

    @DisplayName("404 - DELETE /project/{projectId} with invalid projectId")
    @Test
    void givenInvalidProjectIdWhenDeleteProjectThenReturns404NotFound() throws Exception {
        given(icProjectService.deleteProject(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/project/1"))
                .andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(PROJECT_NOT_FOUND).getMessage());
    }

}
