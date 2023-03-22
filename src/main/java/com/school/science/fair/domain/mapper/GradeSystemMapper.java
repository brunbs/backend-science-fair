package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.GradeSystemResponse;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.entity.GradeSystem;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface GradeSystemMapper {

    GradeSystem requestDtoToEntity (GradeSystemRequestDto gradeSystemRequestDto);
    GradeSystemDto entityToDto(GradeSystem gradeSystemEntity);
    GradeSystemRequestDto requestToRequestDto (CreateGradeSystemRequest gradeSystemRequest);
    GradeSystemResponse dtoToResponse (GradeSystemDto gradeSystemDto);
}
