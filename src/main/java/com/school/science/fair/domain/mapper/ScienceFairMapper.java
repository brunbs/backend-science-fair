package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.ScienceFairListResponse;
import com.school.science.fair.domain.ScienceFairResponse;
import com.school.science.fair.domain.UpdateScienceFairRequest;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.dto.UpdateScienceFairDto;
import com.school.science.fair.domain.entity.ScienceFair;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

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
    List<ScienceFairDto> listEntityToListDto(List<ScienceFair> scienceFairs);
    List<ScienceFairListResponse> listDtoToListResponse(List<ScienceFairDto> scienceFairListDtos);
}
