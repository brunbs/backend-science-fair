package com.school.science.fair.service;

import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.entity.AreaOfKnowledge;
import com.school.science.fair.domain.entity.Topic;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.AreaOfKnowledgeMapper;
import com.school.science.fair.domain.mapper.TopicMapper;
import com.school.science.fair.repository.AreaOfKnowledgeRepository;
import com.school.science.fair.repository.TopicRepository;
import com.school.science.fair.service.impl.AreaOfKnowledgeServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.AREA_OF_KNOWLEDGE_ALREADY_EXISTS;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.AREA_OF_KNOWLEDGE_NOT_FOUND;
import static com.school.science.fair.domain.mother.AreaOfKnowledgeMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class AreaOfKnowledgeServiceImplUnitTest {

    @Spy
    private TopicRepository topicRepository;

    @SpyBean
    private AreaOfKnowledgeRepository areaOfKnowledgeRepository;

    @InjectMocks
    private AreaOfKnowledgeServiceImpl areaOfKnowledgeService;

    @Spy
    private AreaOfKnowledgeMapper areaOfKnowledgeMapper = Mappers.getMapper(AreaOfKnowledgeMapper.class);

    @Spy
    private TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Database save topic and area of knowledge")
    @Test
    void givenClassEntityWhenSaveClassThenReturnSavedEntityWithRightDetails() {
        Topic topicEntity = getTopicEntity();
        topicEntity.setId(null);
        AreaOfKnowledge areaOfKnowledgeEntity = getAreaOfKnowledgeTestEntity();
        areaOfKnowledgeEntity.setId(null);

        Topic storedTopicEntity = testEntityManager.persistAndFlush(topicEntity);

        areaOfKnowledgeEntity.setTopics(Arrays.asList(storedTopicEntity));
        AreaOfKnowledge storedAreaOfKnowledge = testEntityManager.persistAndFlush(areaOfKnowledgeEntity);

        assertThat(topicEntity.getName()).isEqualTo(storedTopicEntity.getName());
        assertThat(topicEntity.getId()).isEqualTo(storedTopicEntity.getId());
        assertThat(areaOfKnowledgeEntity.getId()).isEqualTo(storedAreaOfKnowledge.getId());
        assertThat(areaOfKnowledgeEntity.getName()).isEqualTo(storedAreaOfKnowledge.getName());
        assertThat(storedAreaOfKnowledge.getTopics().get(0)).usingRecursiveComparison().isEqualTo(storedTopicEntity);
    }

    @DisplayName("Create an Area Of Knowledge and Its Topics (existing topics)")
    @Test
    void givenAreaOfKnowledgeDtoWithAListOfTopicsWhenCreateAreaOfKnowledgeThenCreateAreaOfKnowledgeAndTopicsAndReturnAreaOfKnowledgeDto() {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = getCreateAreaOfKnowledgeRequestDto();
        AreaOfKnowledge areaOfKnowledgeToPersist = getAreaOfKnowledgeEntity();
        areaOfKnowledgeToPersist.setId(null);
        AreaOfKnowledge savedAreaOfKnowledge = getAreaOfKnowledgeEntity();
        AreaOfKnowledgeDto expectedAreaOfKnowledgeDto = getAreaOfKnowledgeDto();

        given(areaOfKnowledgeRepository.findByName(anyString())).willReturn(Optional.empty());
        given(areaOfKnowledgeMapper.requestDtoToEntity(areaOfKnowledgeRequestDto)).willReturn(areaOfKnowledgeToPersist);
        given(areaOfKnowledgeRepository.save(areaOfKnowledgeToPersist)).willReturn(savedAreaOfKnowledge);

        AreaOfKnowledgeDto returnedAreaOfKnowledge = areaOfKnowledgeService.createAreaOfKnowledge(areaOfKnowledgeRequestDto);

        assertThat(returnedAreaOfKnowledge).usingRecursiveComparison().isEqualTo(expectedAreaOfKnowledgeDto);
    }

    @DisplayName("Throws ResourceAlreadyFoundException when Create Area Of Knowledge that already exists")
    @Test
    void givenExistinAreaOfKnowledgeNameWhenCreateAreaOfKnowledgeThenThrowsResourceAlreadyExistsExceptionWithCorrectMessage() {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = getCreateAreaOfKnowledgeRequestDto();

        given(areaOfKnowledgeRepository.findByName(anyString())).willReturn(Optional.of(mock(AreaOfKnowledge.class)));

        assertThatThrownBy(
                () -> areaOfKnowledgeService.createAreaOfKnowledge(areaOfKnowledgeRequestDto)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(AREA_OF_KNOWLEDGE_ALREADY_EXISTS.getMessageKey());
    }

    @DisplayName("Create an Area Of Knowledge and Its Topics (new topics)")
    @Test
    void givenAreaOfKnowledgeDtoWithAListOfNewTopicsWhenCreateAreaOfKnowledgeThenCreateAreaOfKnowledgeAndTopicsAndReturnAreaOfKnowledgeDto() {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = getCreateAreaOfKnowledgeRequestDto();
        Topic topicAEntity = getTopicEntity();
        Topic topicBEntity = getTopicEntity();
        topicBEntity.setId(2l);
        topicBEntity.setName("Topic B");
        AreaOfKnowledge areaOfKnowledgeToPersist = getAreaOfKnowledgeEntity();
        areaOfKnowledgeToPersist.getTopics().get(0).setId(null);
        areaOfKnowledgeToPersist.getTopics().get(1).setId(null);
        areaOfKnowledgeToPersist.setId(null);
        AreaOfKnowledge savedAreaOfKnowledge = getAreaOfKnowledgeEntity();
        AreaOfKnowledgeDto expectedAreaOfKnowledgeDto = getAreaOfKnowledgeDto();

        given(areaOfKnowledgeRepository.findByName(anyString())).willReturn(Optional.empty());
        given(areaOfKnowledgeMapper.requestDtoToEntity(areaOfKnowledgeRequestDto)).willReturn(areaOfKnowledgeToPersist);
        given(topicRepository.save(areaOfKnowledgeToPersist.getTopics().get(0))).willReturn(topicAEntity);
        given(topicRepository.save(areaOfKnowledgeToPersist.getTopics().get(1))).willReturn(topicBEntity);
        given(areaOfKnowledgeRepository.save(areaOfKnowledgeToPersist)).willReturn(savedAreaOfKnowledge);

        AreaOfKnowledgeDto returnedAreaOfKnowledge = areaOfKnowledgeService.createAreaOfKnowledge(areaOfKnowledgeRequestDto);

        assertThat(returnedAreaOfKnowledge).usingRecursiveComparison().isEqualTo(expectedAreaOfKnowledgeDto);
        verify(topicRepository, times(2)).save(any(Topic.class));
    }

    @DisplayName("Get an existing Area of Knowledge")
    @Test
    void givenExistingAreaOfKnowledgeWhenGetAreaOfKnowledgeThenReturnAreaOfKnowledgeDto() {
        AreaOfKnowledge areaOfKnowledgeEntity = getAreaOfKnowledgeEntity();

        given(areaOfKnowledgeRepository.findById(anyLong())).willReturn(Optional.of(areaOfKnowledgeEntity));

        AreaOfKnowledgeDto returnedAreaOfKnowledgeDto = areaOfKnowledgeService.getAreaOfKnowledge(1l);

        assertThat(returnedAreaOfKnowledgeDto).usingRecursiveComparison().isEqualTo(areaOfKnowledgeEntity);
    }

    @DisplayName("Throw ResourceNotFoundException when get an nonexistent area of knowledge")
    @Test
    void givenNonexistentAreaOfKnowledgeWhenGetAreaOfKnowledgeThenThrowsResourceNotFoundExceptionWithCorrectMessage() {
        given(areaOfKnowledgeRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> areaOfKnowledgeService.getAreaOfKnowledge(1l)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(AREA_OF_KNOWLEDGE_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Update Area of Knowledge")
    @Test
    void givenAreaOfKnowledgeRequestWhenUpdateAreaOfKnowledgeThenReturnAreaOfKnowledgeDto() {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = getCreateAreaOfKnowledgeRequestDto();
        AreaOfKnowledge areaOfKnowledgeToPersist = getAreaOfKnowledgeEntity();
        areaOfKnowledgeToPersist.setId(null);
        AreaOfKnowledge savedAreaOfKnowledge = getAreaOfKnowledgeEntity();
        AreaOfKnowledgeDto expectedAreaOfKnowledgeDto = getAreaOfKnowledgeDto();

        given(areaOfKnowledgeRepository.findById(anyLong())).willReturn(Optional.of(areaOfKnowledgeToPersist));
        given(areaOfKnowledgeMapper.requestDtoToEntity(areaOfKnowledgeRequestDto)).willReturn(areaOfKnowledgeToPersist);
        given(areaOfKnowledgeRepository.save(areaOfKnowledgeToPersist)).willReturn(savedAreaOfKnowledge);

        AreaOfKnowledgeDto returnedAreaOfKnowledge = areaOfKnowledgeService.updateAreaOfKnowledge(1l, areaOfKnowledgeRequestDto);

        assertThat(returnedAreaOfKnowledge).usingRecursiveComparison().isEqualTo(expectedAreaOfKnowledgeDto);
    }

    @DisplayName("Throws ResourceNotFoundException when update area of knowledge with invalid id")
    @Test
    void givenNonexistentAreaOfKnowledgeWhenUpdateAreaOfKnowledgeThenThrowsResourceNotFoundExceptionWithCorrectMessage() {
        AreaOfKnowledgeRequestDto areaOfKnowledgeRequestDto = getCreateAreaOfKnowledgeRequestDto();

        given(areaOfKnowledgeRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> areaOfKnowledgeService.updateAreaOfKnowledge(1l, areaOfKnowledgeRequestDto)).isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage(AREA_OF_KNOWLEDGE_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get all areas of knowledge")
    @Test
    void givenExistingAreaOfKnowledgeWhenGetAllAreasOfKnowledgeThenReturnAreaOfKnowledgeDtoList() {

        given(areaOfKnowledgeRepository.findAll()).willReturn(getAllAreaOfKnowledgeList());
        List<AreaOfKnowledgeDto> returnedAreasOfKnowledge = areaOfKnowledgeService.getAllAreasOfKnowledge();

        assertThat(returnedAreasOfKnowledge.size()).isEqualTo(3);
    }

    @DisplayName("Get active areas of knowledge")
    @Test
    void givenExistingAreaOfKnowledgeWhenGetAllActiveAreasOfKnowledgeThenReturnAreaOfKnowledgeDtoList() {

        testEntityManager.persist(AreaOfKnowledge.builder().active(true).build());
        testEntityManager.persist(AreaOfKnowledge.builder().active(false).build());
        testEntityManager.persist(AreaOfKnowledge.builder().active(true).build());

        List<AreaOfKnowledgeDto> areasOfKnowledge = areaOfKnowledgeService.getAllActiveAreasOfKnowledge();

        for(AreaOfKnowledgeDto returnedAreasOfKnowledge : areasOfKnowledge) {
            assertThat(returnedAreasOfKnowledge.isActive()).isTrue();
        }
    }

    @DisplayName("Should set active false when delete an area of knowledge")
    @Test
    void givenActiveAreaOfKnowledgeWhenDeleteAreaOfKnowledgeThenReturnAreaOfKnowledgeWithActiveFalse() {
        AreaOfKnowledge createdAreaOfKnowledge = testEntityManager.persist(AreaOfKnowledge.builder().active(true).build());

        AreaOfKnowledgeDto deletedAreaOfKnowledge = areaOfKnowledgeService.deleteAreaOfKnowledge(createdAreaOfKnowledge.getId());

        assertThat(deletedAreaOfKnowledge.isActive()).isFalse();
    }

    @DisplayName("Get all Topics")
    @Test
    void givenValidTopicsWhenGetTopicsThenReturnTopicDtoList() {
        List<Topic> topicsEntities = getTopicsEntityList();

        given(topicRepository.findAll()).willReturn(topicsEntities);

        List<TopicDto> returnedTopicDtos = areaOfKnowledgeService.getAllTopics();

        assertThat(returnedTopicDtos).usingRecursiveComparison().isEqualTo(topicsEntities);
    }

}
