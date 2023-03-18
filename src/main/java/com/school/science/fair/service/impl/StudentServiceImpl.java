package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.entity.Student;
import com.school.science.fair.domain.mapper.StudentMapper;
import com.school.science.fair.repository.StudentRepository;
import com.school.science.fair.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public StudentDto createStudent(StudentRequestDto createStudentDto) {
        Student studentToCreate = studentMapper.createDtoToEntity(createStudentDto);
        studentToCreate.setPassword(studentToCreate.getRegistration().toString());
        studentToCreate.setActive(true);
        Student createdStudent = studentRepository.save(studentToCreate);
        return studentMapper.entityToDto(createdStudent);
    }
}
