package com.school.science.fair.controller;

import com.school.science.fair.api.AreaOfKnowledgeApi;
import com.school.science.fair.domain.AreaOfKnowledgeResponse;
import com.school.science.fair.domain.CreateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.mapper.AreaOfKnowledgeMapper;
import com.school.science.fair.service.AreaOfKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaOfKnowledgeController implements AreaOfKnowledgeApi {

    @Autowired
    private AreaOfKnowledgeService areaOfKnowledgeService;

    @Autowired
    private AreaOfKnowledgeMapper areaOfKnowledgeMapper;

    @Override
    public ResponseEntity<AreaOfKnowledgeResponse> createAreaOfKnowledge(CreateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest) {

        AreaOfKnowledgeRequestDto areaOfKnowledgeToCreate = areaOfKnowledgeMapper.createRequestToRequestDto(createAreaOfKnowledgeRequest);
        AreaOfKnowledgeDto createdAreaOfKnowledge = areaOfKnowledgeService.createAreaOfKnowledge(areaOfKnowledgeToCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(areaOfKnowledgeMapper.dtoToResponse(createdAreaOfKnowledge));
    }
}
