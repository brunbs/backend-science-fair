package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.TopicDto;
import com.school.science.fair.domain.entity.ScienceFair;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface ScienceFairMapper {

    ScienceFair requestDtoToEntity (ScienceFairRequestDto scienceFairRequestDto);
    ScienceFairDto entityToDto(ScienceFair scienceFair);
    ScienceFairRequestDto createRequestToRequestDto(CreateScienceFairRequest createScienceFairRequest);
    ScienceFairResponse dtoToResponse(ScienceFairDto scienceFairDto);
}
