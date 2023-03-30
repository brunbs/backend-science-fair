package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.*;
import com.school.science.fair.domain.dto.AreaOfKnowledgeDto;
import com.school.science.fair.domain.dto.AreaOfKnowledgeRequestDto;
import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.entity.AreaOfKnowledge;
import com.school.science.fair.domain.entity.Topic;

import java.util.Arrays;
import java.util.List;

public class AreaOfKnowledgeMother {

    public static Topic getTopicEntity() {
        return Topic.builder()
                .id(1l)
                .name("Topic A")
                .build();
    }

    public static AreaOfKnowledge getAreaOfKnowledgeTestEntity() {
        return AreaOfKnowledge.builder()
                .id(1l)
                .name("Area A")
                .active(true)
                .build();
    }

    public static AreaOfKnowledge getAreaOfKnowledgeEntity() {
        return AreaOfKnowledge.builder()
                .id(1l)
                .name("Area A")
                .topics(getTopicsEntityList())
                .build();

    }

    public static List<Topic> getTopicsEntityList() {
        return Arrays.asList(
                Topic.builder().id(1l).name("Topic A").build(),
                Topic.builder().id(2l).name("Topic B").build()
        );
    }

    public static AreaOfKnowledgeRequestDto getCreateAreaOfKnowledgeRequestDto() {
        return AreaOfKnowledgeRequestDto.builder()
                .name("Area A")
                .topics(getTopicsDto())
                .build();
    }

    public static AreaOfKnowledgeDto getAreaOfKnowledgeDto() {
        return AreaOfKnowledgeDto.builder()
                .id(1l)
                .name("Area A")
                .topics(getTopicsDto())
                .build();
    }

    public static List<TopicDto> getTopicsDto() {
        return Arrays.asList(
                TopicDto.builder().id(1l).name("Topic A").build(),
                TopicDto.builder().id(2l).name("Topic B").build());
    }

    public static TopicDto getATopicDto() {
        return TopicDto.builder().id(1l).name("Topic A").build();
    }

    public static List<AreaOfKnowledge> getAllAreaOfKnowledgeList() {
        AreaOfKnowledge areaA = getAreaOfKnowledgeEntity();
        areaA.setActive(true);
        AreaOfKnowledge areaB = getAreaOfKnowledgeEntity();
        areaB.setName("Area B");
        areaB.setActive(false);
        areaB.setId(2l);
        AreaOfKnowledge areaC = getAreaOfKnowledgeEntity();
        areaC.setName("Area C");
        areaC.setId(3l);
        areaC.setActive(true);

        return Arrays.asList(
                areaA, areaB, areaC);
    }

    public static List<TopicRequest> getTopicRequest() {
        return Arrays.asList(
                TopicRequest.builder().id(1l).name("Topic A").build(),
                TopicRequest.builder().id(2l).name("Topic B").build()
        );
    }

    public static CreateAreaOfKnowledgeRequest getCreateAreaOfKnowledgeRequest() {
        return CreateAreaOfKnowledgeRequest.builder()
                .name("Area A")
                .topics(getTopicRequest())
                .build();
    }

    public static AreaOfKnowledgeResponse getAreaOfKnowledgeResponse() {
        return AreaOfKnowledgeResponse.builder()
                .id(1l)
                .name("Area A")
                .active(true)
                .topics(getTopicResponseList())
                .build();
    }

    public static List<TopicResponse> getTopicResponseList() {
        return Arrays.asList(
                TopicResponse.builder().id(1l).name("Topic A").build(),
                TopicResponse.builder().id(2l).name("Topic B").build()
        );
    }

    public static List<AreaOfKnowledgeDto> getAreaOfKnowledgeDtoList() {
        return Arrays.asList(
                AreaOfKnowledgeDto.builder().id(1l).name("Area A").active(true).topics(getTopicsDto()).build(),
                AreaOfKnowledgeDto.builder().id(2l).name("Area B").active(false).topics(getTopicsDto()).build(),
                AreaOfKnowledgeDto.builder().id(3l).name("Area C").active(true).topics(getTopicsDto()).build()
        );
    }

    public static UpdateAreaOfKnowledgeRequest getUpdateAreaOfKnowledgeRequest() {
        return UpdateAreaOfKnowledgeRequest.builder()
                .name("New Name")
                .topics(getTopicRequest())
                .build();
    }

}
