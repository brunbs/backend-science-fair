package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.entity.Topic;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface TopicMapper {

    List<Topic> listDtoToListEntity (List<TopicDto> topicDto);
}
