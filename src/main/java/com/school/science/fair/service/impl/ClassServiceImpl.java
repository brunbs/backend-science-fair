package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
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

    private Class findClassOrThrowException(Long id) {
        Optional<Class> foundClassEntity = classRepository.findById(id);
        if(foundClassEntity.isEmpty()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.CLASS_NOT_FOUND);
        }
        return foundClassEntity.get();
    }

    @Override
    public ClassDto createClass(ClassRequestDto classRequestDto) {
        Class classEntity = classMapper.createDtoToEntity(classRequestDto);
        classEntity.setActive(true);
        Class createdClass = classRepository.save(classEntity);
        return classMapper.entityToDto(createdClass);
    }

    public ClassDto getClass(Long classId) {
        Class foundClass = findClassOrThrowException(classId);
        return classMapper.entityToDto(foundClass);
    }

    @Override
    public List<ClassDto> getAllActiveClasses() {
        List<Class> classes = classRepository.findAllByActiveIsTrue();
        return classMapper.toListDto(classes);
    }

    @Override
    public ClassDto deleteClass(Long classId) {
        Class foundClass = findClassOrThrowException(classId);
        foundClass.setActive(false);
        Class deletedClass = classRepository.save(foundClass);
        return classMapper.entityToDto(deletedClass);
    }

    @Override
    public ClassDto updateClass(Long classId, ClassRequestDto classRequestDto) {
        Class foundClass = findClassOrThrowException(classId);
        classMapper.updateModelFromDto(classRequestDto, foundClass);
        Class updatedClass = classRepository.save(foundClass);
        return classMapper.entityToDto(updatedClass);
    }

}
