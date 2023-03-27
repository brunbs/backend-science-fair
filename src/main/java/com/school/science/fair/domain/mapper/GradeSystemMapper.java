package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.GradeSystemResponse;
import com.school.science.fair.domain.UpdateGradeSystemRequest;
import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.entity.Grade;
import com.school.science.fair.domain.entity.GradeSystem;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface GradeSystemMapper {

    GradeSystem requestDtoToEntity (GradeSystemRequestDto gradeSystemRequestDto);
    GradeSystemDto entityToDto(GradeSystem gradeSystemEntity);
    GradeSystemRequestDto requestToRequestDto (CreateGradeSystemRequest gradeSystemRequest);
    GradeSystemResponse dtoToResponse (GradeSystemDto gradeSystemDto);
    List<GradeSystemDto> listEntityToListDto(List<GradeSystem> gradeSystems);
    List<GradeSystemResponse> listDtoToListResponse(List<GradeSystemDto> gradeSystemDtos);
    List<Grade> listDtoToListEntity(List<GradeDto> gradeDto);
    GradeSystem dtoToEntity(GradeSystemDto gradeSystemDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "grades", ignore = true)
    void updateModelFromDto(GradeSystemRequestDto gradeSystemRequestDto, @MappingTarget GradeSystem entity);
    GradeSystemRequestDto updateRequestToDto(UpdateGradeSystemRequest updateGradeSystemRequest);
}
