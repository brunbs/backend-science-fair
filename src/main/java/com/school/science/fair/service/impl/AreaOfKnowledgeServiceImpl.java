package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.entity.AreaOfKnowledge;
import com.school.science.fair.domain.entity.Topic;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.AreaOfKnowledgeMapper;
import com.school.science.fair.repository.AreaOfKnowledgeRepository;
import com.school.science.fair.repository.TopicRepository;
import com.school.science.fair.service.AreaOfKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AreaOfKnowledgeServiceImpl implements AreaOfKnowledgeService {

    @Autowired
    private AreaOfKnowledgeMapper areaOfKnowledgeMapper;

    @Autowired
    private AreaOfKnowledgeRepository areaOfKnowledgeRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Override
    public AreaOfKnowledgeDto createAreaOfKnowledge(AreaOfKnowledgeRequestDto createAreaOfKnowledgeRequestDto) {
        throwExceptionIfAreaOfKnowledgeNameAlreadyExists(createAreaOfKnowledgeRequestDto.getName());
        AreaOfKnowledge areaOfKnowledgeEntity = areaOfKnowledgeMapper.requestDtoToEntity(createAreaOfKnowledgeRequestDto);
        areaOfKnowledgeEntity.setTopics(getListOfTopicsToSave(areaOfKnowledgeEntity.getTopics()));
        AreaOfKnowledge savedAreaOfKnowledge = areaOfKnowledgeRepository.save(areaOfKnowledgeEntity);
        return areaOfKnowledgeMapper.entityToDto(savedAreaOfKnowledge);
    }

    @Override
    public AreaOfKnowledgeDto getAreaOfKnowledge(Long id) {
        AreaOfKnowledge foundAreaOfKnowledge = findAreaOfKnowledgeOrThrowException(id);
        return areaOfKnowledgeMapper.entityToDto(foundAreaOfKnowledge);
    }

    private void throwExceptionIfAreaOfKnowledgeNameAlreadyExists(String areaOfKnowledgeName) {
        Optional<AreaOfKnowledge> foundAreaOfKnowledge = areaOfKnowledgeRepository.findByName(areaOfKnowledgeName);
        if(foundAreaOfKnowledge.isPresent()) {
            throw new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, ExceptionMessage.AREA_OF_KNOWLEDGE_ALREADY_EXISTS);
        }
    }

    private AreaOfKnowledge findAreaOfKnowledgeOrThrowException(Long id) {
        Optional<AreaOfKnowledge> foundAreaOfKnowledge = areaOfKnowledgeRepository.findById(id);
        if(foundAreaOfKnowledge.isPresent()) {
            return foundAreaOfKnowledge.get();
        }
        throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.AREA_OF_KNOWLEDGE_NOT_FOUND);
    }

    private List<Topic> getListOfTopicsToSave(List<Topic> topics) {
        List<Topic> topicsToReturn = new ArrayList<>();
        for(Topic topic : topics) {
            if(topic.getId() == null) {
                Topic createdTopic = topicRepository.save(topic);
                topicsToReturn.add(createdTopic);
            } else {
                topicsToReturn.add(topic);
            }
        }
        return topicsToReturn;
    }
}
