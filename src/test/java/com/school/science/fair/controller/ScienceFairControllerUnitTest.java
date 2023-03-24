package com.school.science.fair.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.service.impl.ScienceFairServiceImpl;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_SYSTEM_NOT_FOUND;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.SCIENCE_FAIR_NOT_FOUND;
import static com.school.science.fair.domain.mother.ScienceFairMother.getCreateScienceFairRequest;
import static com.school.science.fair.domain.mother.ScienceFairMother.getScienceFairDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class ScienceFairControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ScienceFairServiceImpl scienceFairService;

    @SpyBean
    private ObjectMapper mapper;

    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 POST /science-fair")
    @Test
    void givenValidCreateScienceFairRequestWhenCreateScienceFairThenReturn200OkAndCreatedScienceFair() throws Exception {

        CreateScienceFairRequest createScienceFairRequest = getCreateScienceFairRequest();
        ScienceFairDto scienceFairDto = getScienceFairDto();

        given(scienceFairService.createScienceFair(any(ScienceFairRequestDto.class))).willReturn(scienceFairDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/science-fair")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createScienceFairRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        ScienceFairResponse returnedScienceFair = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ScienceFairResponse.class);

        assertThat(returnedScienceFair).usingRecursiveComparison().ignoringFields("gradeSystem", "gradeSystemId", "id").isEqualTo(createScienceFairRequest);
        assertThat(returnedScienceFair.getGradeSystem().getId()).isEqualTo(createScienceFairRequest.getGradeSystemId());
    }

    @DisplayName("404 POST /science-fair - Invalid Grade System Returns 404 Not Found")
    @Test
    void givenCreateScienceFairWithInvalidGradeSystemIdWhenCreateScienceFairThenReturn404NotFound() throws Exception {
        CreateScienceFairRequest createScienceFairRequest = getCreateScienceFairRequest();

        given(scienceFairService.createScienceFair(any(ScienceFairRequestDto.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.GRADE_SYSTEM_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/science-fair")
                .content(new ObjectMapper().writeValueAsString(createScienceFairRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(GRADE_SYSTEM_NOT_FOUND).getMessage());
    }

    @DisplayName("400 POST /science-fair - CreateScienceFair with no name/year/gradeSystemId")
    @Test
    void givenCreateScienceFairWithInvalidArgumentsWhenCreateScienceFairThenReturn400BadRequest() throws Exception {
        CreateScienceFairRequest createScienceFairRequestWithNoName = getCreateScienceFairRequest();
        createScienceFairRequestWithNoName.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/science-fair")
                        .content(new ObjectMapper().writeValueAsString(createScienceFairRequestWithNoName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

        CreateScienceFairRequest createScienceFairRequestWithNoEditionYear = getCreateScienceFairRequest();
        createScienceFairRequestWithNoEditionYear.setEditionYear(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/science-fair")
                        .content(new ObjectMapper().writeValueAsString(createScienceFairRequestWithNoEditionYear))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.editionYear", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));

        CreateScienceFairRequest createScienceFairRequestWithNoGradeSystemId = getCreateScienceFairRequest();
        createScienceFairRequestWithNoGradeSystemId.setGradeSystemId(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/science-fair")
                        .content(new ObjectMapper().writeValueAsString(createScienceFairRequestWithNoGradeSystemId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gradeSystemId", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }


    @DisplayName("200 GET /science-fair/{id}")
    @Test
    void givenValidIdWhenGetScienceFairThenReturns200OkAndScienceFairResponse() throws Exception {
        ScienceFairDto foundScienceFair = getScienceFairDto();

        given(scienceFairService.getScienceFair(anyLong())).willReturn(foundScienceFair);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/science-fair/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        ScienceFairResponse scienceFairResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ScienceFairResponse.class);

        assertThat(foundScienceFair).usingRecursiveComparison().isEqualTo(scienceFairResponse);
    }

    @DisplayName("404 GET /science-fair/{id} with invalid id")
    @Test
    void givenInvalidIdWhenGetScienceFairThenReturns404NotFoundAndCorrectMessage() throws Exception {
        given(scienceFairService.getScienceFair(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, SCIENCE_FAIR_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/science-fair/1"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(SCIENCE_FAIR_NOT_FOUND).getMessage());
    }

}
