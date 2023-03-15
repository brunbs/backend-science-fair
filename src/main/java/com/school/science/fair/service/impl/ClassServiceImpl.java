package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.CreateClassDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.repository.ClassRepository;
import com.school.science.fair.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    private ClassDto findClassOrThrowException(Long id) {
        Optional<Class> foundClassEntity = classRepository.findById(id);
        if(foundClassEntity.isEmpty()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.CLASS_NOT_FOUND);
        }
        return classMapper.entityToDto(foundClassEntity.get());
    }

    @Override
    public ClassDto createClass(CreateClassDto createClassDto) {

        Class classEntity = classMapper.dtoToEntity(createClassDto);
        classEntity.setActive(true);
        Class createdClass = classRepository.save(classEntity);
        return classMapper.entityToDto(createdClass);
    }

    public ClassDto getClass(Long id) {
        ClassDto foundClass = findClassOrThrowException(id);
        return foundClass;
    }

    @Override
    public List<ClassDto> getAllActiveClasses() {
        List<Class> classes = classRepository.findAllByActiveIsTrue();
        return classMapper.toListDto(classes);
    }

}
