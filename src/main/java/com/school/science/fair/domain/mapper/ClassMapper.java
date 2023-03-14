package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.CreateClassDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.GradeYearEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface ClassMapper {

    Class dtoToEntity(CreateClassDto createClassDto);
    ClassDto entityToDto(Class classEntity);
    CreateClassDto requestToDto(CreateClassRequest createClassRequest);
    @Mapping(source = "gradeYear", target = "gradeYear", qualifiedByName = "getEnumDescription")
    ClassResponse dtoToResponse(ClassDto classDto);

    @Named("getEnumDescription")
    public static String getEnumDescription(GradeYearEnum gradeYearEnum) {
        return gradeYearEnum.getDescription();
    }
}
