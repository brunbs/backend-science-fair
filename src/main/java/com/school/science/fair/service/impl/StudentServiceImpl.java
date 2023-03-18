package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.entity.Student;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.StudentMapper;
import com.school.science.fair.repository.StudentRepository;
import com.school.science.fair.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public StudentDto createStudent(StudentRequestDto createStudentDto) {
        findStudentByEmail(createStudentDto.getEmail());
        Student studentToCreate = studentMapper.createDtoToEntity(createStudentDto);
        studentToCreate.setPassword(studentToCreate.getRegistration().toString());
        studentToCreate.setActive(true);
        Student createdStudent = studentRepository.save(studentToCreate);
        return studentMapper.entityToDto(createdStudent);
    }

    @Override
    public StudentDto getStudent(Long studentRegistration) {
        Student foundStudent = findStudentOrThrowException(studentRegistration);
        return studentMapper.entityToDto(foundStudent);
    }

    private Student findStudentOrThrowException(Long id) {
        Optional<Student> foundStudentEntity = studentRepository.findById(id);
        if(foundStudentEntity.isEmpty()) {
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.STUDENT_NOT_FOUND);
        }
        return foundStudentEntity.get();
    }

    private void findStudentByEmail(String email) {
        Optional<Student> foundStudentEntity = studentRepository.findByEmail(email);
        if(!foundStudentEntity.isEmpty()) {
            throw new ResourceAlreadyExistsException(HttpStatus.BAD_REQUEST, ExceptionMessage.STUDENT_ALREADY_EXISTS);
        }
    }


}
