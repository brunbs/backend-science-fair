package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.UpdateScienceFairRequest;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.UpdateScienceFairDto;
import com.school.science.fair.domain.entity.ScienceFair;
import org.mapstruct.*;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface ScienceFairMapper {

    ScienceFair requestDtoToEntity (ScienceFairRequestDto scienceFairRequestDto);
    ScienceFairDto entityToDto(ScienceFair scienceFair);
    ScienceFairRequestDto createRequestToRequestDto(CreateScienceFairRequest createScienceFairRequest);
    ScienceFairResponse dtoToResponse(ScienceFairDto scienceFairDto);

    UpdateScienceFairDto updateRequestToRequestDto(UpdateScienceFairRequest updateScienceFairRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(UpdateScienceFairDto updateScienceFairDto, @MappingTarget ScienceFair entity);
}
