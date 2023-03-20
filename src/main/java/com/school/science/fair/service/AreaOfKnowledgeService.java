package com.school.science.fair.service;

import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;

import java.util.List;

public interface AreaOfKnowledgeService {

    AreaOfKnowledgeDto createAreaOfKnowledge(AreaOfKnowledgeRequestDto createAreaOfKnowledgeRequestDto);
    AreaOfKnowledgeDto getAreaOfKnowledge(Long id);
    AreaOfKnowledgeDto updateAreaOfKnowledge(Long id, AreaOfKnowledgeRequestDto updateAreaOfKnowledgeRequestDto);
    List<AreaOfKnowledgeDto> getAllAreasOfKnowledge();
    AreaOfKnowledgeDto deleteAreaOfKnowledge(Long id);
    List<AreaOfKnowledgeDto> getAllActiveAreasOfKnowledge();

}
