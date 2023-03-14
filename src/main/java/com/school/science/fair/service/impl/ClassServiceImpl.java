package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.CreateClassDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.repository.ClassRepository;
import com.school.science.fair.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Override
    public ClassDto createClass(CreateClassDto createClassDto) {

        Class classEntity = classMapper.dtoToEntity(createClassDto);
        Class createdClass = classRepository.save(classEntity);
        return classMapper.entityToDto(createdClass);
    }
}
