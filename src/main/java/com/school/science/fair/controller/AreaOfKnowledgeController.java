package com.school.science.fair.controller;

import com.school.science.fair.api.AreaOfKnowledgeApi;
import com.school.science.fair.domain.AreaOfKnowledgeResponse;
import com.school.science.fair.domain.CreateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.TopicResponse;
import com.school.science.fair.domain.UpdateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.mapper.AreaOfKnowledgeMapper;
import com.school.science.fair.domain.mapper.TopicMapper;
import com.school.science.fair.service.AreaOfKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AreaOfKnowledgeController implements AreaOfKnowledgeApi {

    @Autowired
    private AreaOfKnowledgeService areaOfKnowledgeService;

    @Autowired
    private AreaOfKnowledgeMapper areaOfKnowledgeMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public ResponseEntity<AreaOfKnowledgeResponse> createAreaOfKnowledge(CreateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest) {

        AreaOfKnowledgeRequestDto areaOfKnowledgeToCreate = areaOfKnowledgeMapper.createRequestToRequestDto(createAreaOfKnowledgeRequest);
        AreaOfKnowledgeDto createdAreaOfKnowledge = areaOfKnowledgeService.createAreaOfKnowledge(areaOfKnowledgeToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(areaOfKnowledgeMapper.dtoToResponse(createdAreaOfKnowledge));
    }

    @Override
    public ResponseEntity<AreaOfKnowledgeResponse> getAreaOfKnowledge(Long id) {
        AreaOfKnowledgeDto foundAreaOfKnowledge = areaOfKnowledgeService.getAreaOfKnowledge(id);
        return ResponseEntity.ok().body(areaOfKnowledgeMapper.dtoToResponse(foundAreaOfKnowledge));
    }

    @Override
    public ResponseEntity<AreaOfKnowledgeResponse> updateAreaOfKnowledge(Long id, UpdateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest) {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = areaOfKnowledgeMapper.updateRequestToRequestDto(createAreaOfKnowledgeRequest);
        AreaOfKnowledgeDto updatedAreaOfKnowledgeDto =areaOfKnowledgeService.updateAreaOfKnowledge(id, areaOfKnowledgeRequestDto);
        return ResponseEntity.ok().body(areaOfKnowledgeMapper.dtoToResponse(updatedAreaOfKnowledgeDto));
    }

    @Override
    public ResponseEntity<List<AreaOfKnowledgeResponse>> getAllAreasOfKnowledge() {
        List<AreaOfKnowledgeDto> foundAreasOfKnowledgeDto = areaOfKnowledgeService.getAllAreasOfKnowledge();
        return ResponseEntity.ok().body(areaOfKnowledgeMapper.listDtoToListResponse(foundAreasOfKnowledgeDto));
    }

    @Override
    public ResponseEntity<AreaOfKnowledgeResponse> deleteAreaOfKnowledge(Long id) {
        AreaOfKnowledgeDto deactivatedAreaOfKnowledge = areaOfKnowledgeService.deleteAreaOfKnowledge(id);
        return ResponseEntity.ok().body(areaOfKnowledgeMapper.dtoToResponse(deactivatedAreaOfKnowledge));
    }

    @Override
    public ResponseEntity<List<AreaOfKnowledgeResponse>> getAllActiveAreasOfKnowledge() {
        List<AreaOfKnowledgeDto> foundActiveAreasOfKnowledgeDto = areaOfKnowledgeService.getAllActiveAreasOfKnowledge();
        return ResponseEntity.ok().body(areaOfKnowledgeMapper.listDtoToListResponse(foundActiveAreasOfKnowledgeDto));
    }

    @Override
    public ResponseEntity<List<TopicResponse>> getAllAreaOfKnowledgeTopics() {
        List<TopicDto> foundTopics = areaOfKnowledgeService.getAllTopics();
        return ResponseEntity.ok().body(topicMapper.listDtoToListResponse(foundTopics));
    }
}
