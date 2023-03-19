package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.AreaOfKnowledgeResponse;
import com.school.science.fair.domain.CreateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.UpdateAreaOfKnowledgeRequest;
import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.entity.AreaOfKnowledge;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface AreaOfKnowledgeMapper {

    AreaOfKnowledge requestDtoToEntity(AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto);
    AreaOfKnowledgeDto entityToDto(AreaOfKnowledge areaOfKnowledge);
    AreaOfKnowledgeRequestDto createRequestToRequestDto(CreateAreaOfKnowledgeRequest createAreaOfKnowledgeRequest);
    AreaOfKnowledgeResponse dtoToResponse(AreaOfKnowledgeDto areaOfKnowledgeDto);
    AreaOfKnowledgeRequestDto updateRequestToRequestDto(UpdateAreaOfKnowledgeRequest updateAreaOfKnowledgeRequest);
}
