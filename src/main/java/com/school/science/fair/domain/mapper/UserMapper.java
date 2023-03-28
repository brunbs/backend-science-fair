package com.school.science.fair.domain.mapper;

import com.school.science.fair.domain.CreateUserRequest;
import com.school.science.fair.domain.UpdateStudentRequest;
import com.school.science.fair.domain.UserResponse;
import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.UserRequestDto;
import com.school.science.fair.domain.entity.Users;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = IGNORE)
public interface UserMapper {

    Users createDtoToEntity(UserRequestDto userRequestDto);
    UserDto entityToDto(Users users);
    UserRequestDto createRequestToDto(CreateUserRequest createStudentRequest);
    UserResponse dtoToResponse(UserDto userDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromDto(UserRequestDto userRequestDto, @MappingTarget Users entity);
    UserRequestDto updateToDto(UpdateStudentRequest updateStudentRequest);

}
