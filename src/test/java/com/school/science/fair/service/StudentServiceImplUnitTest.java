package com.school.science.fair.service;

import com.school.science.fair.domain.dto.StudentDto;
import com.school.science.fair.domain.dto.StudentRequestDto;
import com.school.science.fair.domain.entity.Student;
import com.school.science.fair.domain.exception.ResourceAlreadyExistsException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.StudentMapper;
import com.school.science.fair.repository.StudentRepository;
import com.school.science.fair.service.impl.StudentServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.*;
import static com.school.science.fair.domain.mother.StudentMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class StudentServiceImplUnitTest {

    @SpyBean
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Spy
    private StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Database save student")
    @Test
    void givenStudentEntityWhenSaveStudentThenReturnSavedEntityWithRightDetails() {
        Student studentEntity = getStudentEntity();

        Student storedStudentEntity = testEntityManager.persistAndFlush(studentEntity);

        assertThat(studentEntity.getName()).isEqualTo(storedStudentEntity.getName());
        assertThat(studentEntity.getEmail()).isEqualTo(storedStudentEntity.getEmail());
        assertThat(studentEntity.getPassword()).isEqualTo(storedStudentEntity.getPassword());
        assertThat(studentEntity.isActive()).isEqualTo(storedStudentEntity.isActive());
    }

    @DisplayName("Create a student")
    @Test
    void givenValidStudentRequestDtoWhenCreateStudentThenCreatesStudentAndReturnStudentDto() {
        StudentRequestDto studentRequestDto = getStudentRequestDto();
        Student studentEntity = getStudentEntity();
        Student createdStudent = getStudentEntity();
        StudentDto createdStudentDto = getStudentDto();

        given(studentRepository.findByEmailOrRegistration(anyString(), anyLong())).willReturn(Optional.empty());
        given(studentMapper.createDtoToEntity(studentRequestDto)).willReturn(studentEntity);
        given(studentRepository.save(studentEntity)).willReturn(createdStudent);
        given(studentMapper.entityToDto(createdStudent)).willReturn(createdStudentDto);

        StudentDto returnedStudent = studentService.createStudent(studentRequestDto);

        assertThat(returnedStudent).usingRecursiveComparison().isEqualTo(createdStudentDto);

        verify(studentRepository).findByEmailOrRegistration(anyString(), anyLong());
        verify(studentRepository).save(any(Student.class));

    }

    @DisplayName("Create Student when registration or email already exists Should Throw ResourceAlreadyExistsException")
    @Test
    void givenStudentRequestDtoWithEmailOrRegistrationAlreadyInUseWhenCreateStudentThenThrowsResourceAlreadyExistsException() {
        StudentRequestDto studentRequestDto = getStudentRequestDto();

        given(studentRepository.findByEmailOrRegistration(anyString(), anyLong())).willReturn(Optional.of(getStudentEntity()));

        assertThatThrownBy(
                () -> studentService.createStudent(studentRequestDto)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(STUDENT_ALREADY_EXISTS.getMessageKey());
    }

    @DisplayName("Get a student")
    @Test
    void givenValidStudentRegistrationWhenGetStudentThenReturnsStudentDto() {
        Student studentEntity = getStudentEntity();

        given(studentRepository.findById(anyLong())).willReturn(Optional.of(studentEntity));

        StudentDto returnedStudent = studentService.getStudent(1l);

        assertThat(returnedStudent).usingRecursiveComparison().isEqualTo(studentEntity);
    }

    @DisplayName("Get a not existent student should throw ResourceNotFoundException")
    @Test
    void givenInvalidRegistrationWhenGetStudentThenThrowResourceNotFoundExceptionWithCorrectMessage() {

        given(studentRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> studentService.getStudent(1l)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(STUDENT_NOT_FOUND.getMessageKey());

    }

    @DisplayName("Delete student should change active to false")
    @Test
    void givenValidStudentRegistrationWhenDeleteStudentThenChangeEntityActiveToFalseAndReturnStudentDto() {
        Student foundStudent = getStudentEntity();

        given(studentRepository.findById(anyLong())).willReturn(Optional.of(foundStudent));

        StudentDto deletedStudentDto = studentService.deleteStudent(1l);

        assertThat(deletedStudentDto.isActive()).isFalse();

    }

    @DisplayName("Update student")
    @Test
    void givenValidNameStudentRequestDtoWhenUpdateStudentThenReturnStudentDto() {
        Student foundStudent = getStudentEntity();
        StudentRequestDto updateInfo = StudentRequestDto.builder().name("Student B").build();

        given(studentRepository.findById(anyLong())).willReturn(Optional.of(foundStudent));

        StudentDto updatedStudent = studentService.updateStudent(1l, updateInfo);

        assertThat(updatedStudent.getName()).isEqualTo(updateInfo.getName());
        verify(studentRepository).save(foundStudent);

    }

    @DisplayName("Update student")
    @Test
    void givenValidEmailStudentRequestDtoWhenUpdateStudentThenReturnStudentDto() {
        Student foundStudent = getStudentEntity();
        StudentRequestDto updateInfo = StudentRequestDto.builder().email("newemail@email.com").build();

        given(studentRepository.findById(anyLong())).willReturn(Optional.of(foundStudent));
        given(studentRepository.findByEmail(anyString())).willReturn(Optional.empty());

        StudentDto updatedStudent = studentService.updateStudent(1l, updateInfo);

        assertThat(updatedStudent.getEmail()).isEqualTo(updateInfo.getEmail());
        verify(studentRepository).save(foundStudent);

    }

    @DisplayName("Update student")
    @Test
    void givenEmailAlreadyRegisteredWhenUpdateStudentThenThrowsResourceAlreadyExistsExceptionWithCorrectMessage() {
        Student foundStudent = getStudentEntity();

        StudentRequestDto updateInfo = StudentRequestDto.builder().email("newemail@email.com").build();
        given(studentRepository.findById(anyLong())).willReturn(Optional.of(foundStudent));
        given(studentRepository.findByEmail(anyString())).willReturn(Optional.of(foundStudent));

        assertThatThrownBy(
                () -> studentService.updateStudent(1l, updateInfo)).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(EMAIL_ALREADY_EXISTS.getMessageKey());

    }

}
