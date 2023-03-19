package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateStudentRequest;
import com.school.science.fair.domain.StudentResponse;
import com.school.science.fair.domain.UpdateStudentRequest;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.entity.Student;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface StudentMapper {

    Student createDtoToEntity(StudentRequestDto studentRequestDto);
    StudentDto entityToDto(Student student);
    StudentRequestDto createRequestToDto(CreateStudentRequest createStudentRequest);
    StudentResponse dtoToResponse(StudentDto studentDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(StudentRequestDto studentRequestDto, @MappingTarget Student entity);
    StudentRequestDto updateToDto(UpdateStudentRequest updateStudentRequest);

}
