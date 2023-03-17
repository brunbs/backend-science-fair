package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ClassDto;
import com.school.science.fair.domain.dto.ClassRequestDto;
import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.ClassMapper;
import com.school.science.fair.repository.ClassRepository;
import com.school.science.fair.service.impl.ClassServiceImpl;
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

import java.util.List;
import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.CLASS_NOT_FOUND;
import static com.school.science.fair.domain.mother.ClassMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class ClassServiceImplUnitTest {

    @SpyBean
    private ClassRepository classRepository;

    @InjectMocks
    private ClassServiceImpl classService;

    @Spy
    private ClassMapper classMapper = Mappers.getMapper(ClassMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;


    @DisplayName("Database save class")
    @Test
    void givenClassEntityWhenSaveClassThenReturnSavedEntityWithRightDetails() {
        Class classEntity = getClassEntity();
        classEntity.setId(null);

        Class storedClassEntity = testEntityManager.persistAndFlush(classEntity);

        assertThat(classEntity.getName()).isEqualTo(storedClassEntity.getName());
        assertThat(classEntity.getGradeYear()).isEqualTo(storedClassEntity.getGradeYear());
        assertThat(classEntity.isActive()).isEqualTo(storedClassEntity.isActive());
    }


    @DisplayName("Create a class")
    @Test
    void givenValidClassRequestDtoWhenCreateClassThenCreatesClassAndReturnClassDto() {
        ClassRequestDto classRequestDto = getCreateClassRequestDto();
        Class classEntity = getClassEntity();
        Class createdClass = getClassEntity();
        createdClass.setActive(true);
        ClassDto classDto = getClassDto();

        given(classMapper.createDtoToEntity(classRequestDto)).willReturn(classEntity);
        given(classRepository.save(classEntity)).willReturn(createdClass);
        given(classMapper.entityToDto(createdClass)).willReturn(classDto);

        ClassDto returnedClass = classService.createClass(classRequestDto);

        assertThat(returnedClass).usingRecursiveComparison().isEqualTo(classDto);
        verify(classRepository).save(any(Class.class));
    }

    @DisplayName("Get an existing Class")
    @Test
    void givenClassIdWhenGetClassThenReturnClassDto() {
        Class classEntity = getClassEntity();
        given(classRepository.findById(1l)).willReturn(Optional.of(classEntity));

        ClassDto returnedClass = classService.getClass(1l);

        assertThat(returnedClass).usingRecursiveComparison().isEqualTo(classEntity);
    }

    @DisplayName("Should Throw Resource Not Found Exception When Get Inexistent Class")
    @Test
    void givenInvalidClassIdWhenGetClassThenThrowsResourceNotFoundException() {

        given(classRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> classService.getClass(1l)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(CLASS_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Should return only active classes")
    @Test
    void givenPersistedClassesWhenGetAllActiveClassesThenReturnActiveClassesOnly() {
        testEntityManager.persist(Class.builder().active(true).build());
        testEntityManager.persist(Class.builder().active(false).build());
        testEntityManager.persist(Class.builder().active(true).build());

        List<ClassDto> classes = classService.getAllActiveClasses();

        for(ClassDto returnedClass : classes) {
            assertThat(returnedClass.isActive()).isTrue();
        }
    }

    @DisplayName("Should set active false when delete a class")
    @Test
    void givenActiveClassWhenDeleteClassThenReturnClassWithActiveFalse() {
        Class createdClass = testEntityManager.persist(Class.builder().active(true).build());

        ClassDto deletedClass = classService.deleteClass(createdClass.getId());
        assertThat(deletedClass.isActive()).isFalse();
    }

    @DisplayName("Should returned updated class when update a class")
    @Test
    void givenClassRequestDtoWithValuesToUpdateWhenUpdateClassThenReturnUpdatedClass() {
        Class entityInDatabase = getClassEntity();
        entityInDatabase.setId(null);
        ClassRequestDto classToUpdate = getUpdateClassRequestDto();
        Class classInDb = testEntityManager.persist(entityInDatabase);

        ClassDto updatedClass = classService.updateClass(classInDb.getId(), classToUpdate);
        assertThat(updatedClass.getName()).isEqualTo(classToUpdate.getName());
    }

}
