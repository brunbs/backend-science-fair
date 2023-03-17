package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.ClassResponse;
import com.school.science.fair.domain.CreateClassRequest;
import com.school.science.fair.domain.UpdateClassRequest;
import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.GradeYearEnum;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface ClassMapper {

    Class createDtoToEntity(ClassRequestDto classRequestDto);
    ClassDto entityToDto(Class classEntity);
    ClassRequestDto toRequestDto(CreateClassRequest createClassRequest);
    @Mapping(source = "gradeYear", target = "gradeYear", qualifiedByName = "getEnumDescription")
    ClassResponse dtoToResponse(ClassDto classDto);
    List<ClassDto> toListDto(List<Class> classes);
    List<ClassResponse> toClassResponseList(List<ClassDto> classDtos);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(ClassRequestDto classRequestDto, @MappingTarget Class entity);
    ClassRequestDto toRequestDto(UpdateClassRequest updateClassRequest);
    @Named("getEnumDescription")
    public static String getEnumDescription(GradeYearEnum gradeYearEnum) {
        return gradeYearEnum.getDescription();
    }
}
