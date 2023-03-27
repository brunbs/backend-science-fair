package com.school.science.fair.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.science.fair.domain.AreaOfKnowledgeResponse;
import com.school.science.fair.domain.CreateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.TopicResponse;
import com.school.science.fair.domain.UpdateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.builder.ExceptionResponseBuilder;
import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.service.impl.AreaOfKnowledgeServiceImpl;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.AREA_OF_KNOWLEDGE_NOT_FOUND;
import static com.school.science.fair.domain.mother.AreaOfKnowledgeMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableWebMvc
@AutoConfigureMockMvc
public class AreaOfKnowledgeControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AreaOfKnowledgeServiceImpl areaOfKnowledgeService;

    @SpyBean
    private ObjectMapper mapper;

    @Autowired
    private ExceptionResponseBuilder responseBuilder;

    @DisplayName("201 POST /area-of-knowledge")
    @Test
    void givenValidCreateAreaOfKnowledgeRequestWhenCreateAreaOfKnowledgeThenReturn200OkAndCreatedAreaOfKnowledge() throws Exception {

        CreateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest = getCreateAreaOfKnowledgeRequest();
        AreaOfKnowledgeDto areaOfKnowledgeDto = getAreaOfKnowledgeDto();
        AreaOfKnowledgeResponse areaOfKnowledgeResponse = getAreaOfKnowledgeResponse();

        given(areaOfKnowledgeService.createAreaOfKnowledge(any(AreaOfKnowledgeRequestDto.class))).willReturn(areaOfKnowledgeDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/area-of-knowledge")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(createAreaOfKnowledgeRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        AreaOfKnowledgeResponse returnedAreaOfKnowledge = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), AreaOfKnowledgeResponse.class);

        assertThat(returnedAreaOfKnowledge).usingRecursiveComparison().ignoringFields("active").isEqualTo(areaOfKnowledgeResponse);
        assertThat(createAreaOfKnowledgeRequest.getName()).isEqualTo(returnedAreaOfKnowledge.getName());
    }

    @DisplayName("400 POST /area-of-knowledge")
    @Test
    void givenCreateAreaOfKnowledgeWithNoNameWhenCreateAreaOfKnowledgeThenReturn400BadRequest() throws Exception {
        CreateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest = getCreateAreaOfKnowledgeRequest();
        createAreaOfKnowledgeRequest.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/area-of-knowledge")
                        .content(new ObjectMapper().writeValueAsString(createAreaOfKnowledgeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("não deve ser nulo")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("200 GET /area-of-knowledge/all")
    @Test
    void givenAreaOfKnowledgeWhenGetAllAreaOfKnowledgeThenReturn200OkAndAreaOfKnowledgeResponse() throws Exception {

        List<AreaOfKnowledgeDto> areaOfKnowledgeDtos = getAreaOfKnowledgeDtoList();

        given(areaOfKnowledgeService.getAllAreasOfKnowledge()).willReturn(areaOfKnowledgeDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/area-of-knowledge/all"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<AreaOfKnowledgeResponse> areaOfKnowledgeResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<AreaOfKnowledgeResponse>>(){});

        assertThat(areaOfKnowledgeDtos).usingRecursiveComparison().isEqualTo(areaOfKnowledgeResponse);
    }

    @DisplayName("200 GET /area-of-knowledge/{id}")
    @Test
    void givenValidIdWhenGetAreaOfKnowledgeThenReturn200OkAndAreaOfKnowledgeResponse() throws Exception {
        AreaOfKnowledgeDto areaOfKnowledgeDto = getAreaOfKnowledgeDto();

        given(areaOfKnowledgeService.getAreaOfKnowledge(anyLong())).willReturn(areaOfKnowledgeDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/area-of-knowledge/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        AreaOfKnowledgeResponse areaOfKnowledgeResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), AreaOfKnowledgeResponse.class);

        assertThat(areaOfKnowledgeDto).usingRecursiveComparison().isEqualTo(areaOfKnowledgeResponse);
    }

    @DisplayName("404 GET /area-of-knowledge/{id} with invalid Id")
    @Test
    void givenInvalidIdWhenGetAreaOfKnowledgeThenReturn404NotFoundAndCorrectMessage() throws Exception {

        given(areaOfKnowledgeService.getAreaOfKnowledge(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.AREA_OF_KNOWLEDGE_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/area-of-knowledge/1")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(AREA_OF_KNOWLEDGE_NOT_FOUND).getMessage());

    }

    @DisplayName("200 - PATCH /area-of-knowledge/{id} with valid id")
    @Test
    void givenValidIdAndUpdateAreaOfKnowledgeRequestWhenUpdateAreaOfKnowledgeThenReturn200AndUpdatedAreaOfKnowledge() throws Exception {
        UpdateAreaOfKnowledgeRequest updateAreaOfKnowledgeRequest = getUpdateAreaOfKnowledgeRequest();
        AreaOfKnowledgeDto updatedAreaOfKnowledgeDto = getAreaOfKnowledgeDto();
        updatedAreaOfKnowledgeDto.setName(updateAreaOfKnowledgeRequest.getName());

        given(areaOfKnowledgeService.updateAreaOfKnowledge(anyLong(), any(AreaOfKnowledgeRequestDto.class))).willReturn(updatedAreaOfKnowledgeDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/area-of-knowledge/1")
                .content(new ObjectMapper().writeValueAsString(updateAreaOfKnowledgeRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse();

        AreaOfKnowledgeResponse areaOfKnowledgeResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), AreaOfKnowledgeResponse.class);
        assertThat(areaOfKnowledgeResponse.getName()).isEqualTo(updateAreaOfKnowledgeRequest.getName());
    }


    @DisplayName("404 PATCH /area-of-knowledge/{id} with invalid Id")
    @Test
    void givenInvalidIdWhenUpdateAreaOfKnowledgeThenReturn404NotFoundAndCorrectMessage() throws Exception {
        UpdateAreaOfKnowledgeRequest updateAreaOfKnowledgeRequest = getUpdateAreaOfKnowledgeRequest();

        given(areaOfKnowledgeService.updateAreaOfKnowledge(anyLong(), any(AreaOfKnowledgeRequestDto.class))).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.AREA_OF_KNOWLEDGE_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.patch("/area-of-knowledge/1")
                .content(new ObjectMapper().writeValueAsString(updateAreaOfKnowledgeRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andReturn().getResponse();
        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(AREA_OF_KNOWLEDGE_NOT_FOUND).getMessage());
    }

    @DisplayName("200 DELETE - /area-of-knowledge/{id} with valid Id")
    @Test
    void givenValidIdWhenDeleteAreaOfKnowledgeThenReturn200OkAndAreaOfKnowledgeResponseWithActiveFalse() throws Exception {
        AreaOfKnowledgeDto deletedAreaOfKnowledgeDto = getAreaOfKnowledgeDto();
        deletedAreaOfKnowledgeDto.setActive(false);

        given(areaOfKnowledgeService.deleteAreaOfKnowledge(anyLong())).willReturn(deletedAreaOfKnowledgeDto);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/area-of-knowledge/1"))
                .andExpect(status().isOk()).andReturn().getResponse();

        AreaOfKnowledgeResponse areaOfKnowledgeResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), AreaOfKnowledgeResponse.class);

        assertThat(areaOfKnowledgeResponse.getActive()).isFalse();
    }

    @DisplayName("404 DELETE - /area-of-knowledge/{id} with invalid Id")
    @Test
    void givenInvalidIdWhenDeleteAreaOfKnowledgeThenReturn200OkAndAreaOfKnowledgeResponseWithActiveFalse() throws Exception {

        given(areaOfKnowledgeService.deleteAreaOfKnowledge(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, AREA_OF_KNOWLEDGE_NOT_FOUND));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/area-of-knowledge/1"))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(response.getContentAsString(StandardCharsets.UTF_8)).contains(responseBuilder.getExceptionResponse(AREA_OF_KNOWLEDGE_NOT_FOUND).getMessage());
    }

    @DisplayName("200 - GET /area-of-knowledge/all/active")
    @Test
    void givenAreaOfKnowledgeWhenGetAllActiveAreaOfKnowledgeThenReturn200OkAndAreaOfKnowledgeResponse() throws Exception {
        List<AreaOfKnowledgeDto> activeTrueDtos = getAreaOfKnowledgeDtoList().stream().filter(AreaOfKnowledgeDto::isActive).collect(Collectors.toList());

        given(areaOfKnowledgeService.getAllActiveAreasOfKnowledge()).willReturn(activeTrueDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/area-of-knowledge/all/active"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<AreaOfKnowledgeResponse> areaOfKnowledgeResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<AreaOfKnowledgeResponse>>(){});

        assertThat(activeTrueDtos.size()).isEqualTo(areaOfKnowledgeResponse.size());
    }

    @DisplayName("200 - GET /area-of-knowledge/topics/all")
    @Test
    void givenTopicsWhenGetAllTopicsThenReturn200OkAndTopicsResponse() throws Exception {

        List<TopicDto> topicsDtos = getTopicsDto();

        given(areaOfKnowledgeService.getAllTopics()).willReturn(topicsDtos);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/area-of-knowledge/topics/all"))
                .andExpect(status().isOk()).andReturn().getResponse();

        List<TopicResponse> topicsResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<TopicResponse>>(){});

        assertThat(topicsDtos).usingRecursiveComparison().isEqualTo(topicsResponse);
    }
}
