package com.school.science.fair.service;

import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;

public interface AreaOfKnowledgeService {

    AreaOfKnowledgeDto createAreaOfKnowledge(AreaOfKnowledgeRequestDto createAreaOfKnowledgeRequestDto);

}
