package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.entity.Grade;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface GradeMapper {

    Grade requestDtoToEntity (GradeDto gradeDto);
    GradeDto entityToDto(Grade entity);
    List<GradeDto> listEntityToListDto(List<Grade> grades);
}
