package com.school.science.fair.service;

import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.entity.Grade;
import com.school.science.fair.domain.entity.GradeSystem;
import com.school.science.fair.domain.exception.GradeException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.repository.GradeRepository;
import com.school.science.fair.repository.GradeSystemRepository;
import com.school.science.fair.service.impl.GradeSystemServiceImpl;
import org.assertj.core.api.Assertions;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_SYSTEM_NOT_FOUND;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_VALUES_BIGGER_THEN_GRADE_SYSTEM_MAX_VALUE;
import static com.school.science.fair.domain.mother.GradeSystemMother.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class GradeSystemServiceImplUnitTest {

    @Spy
    private GradeRepository gradeRepository;

    @SpyBean
    private GradeSystemRepository gradeSystemRepository;

    @InjectMocks
    private GradeSystemServiceImpl gradeSystemService;

    @Spy
    private GradeSystemMapper gradeSystemMapper = Mappers.getMapper(GradeSystemMapper.class);

    @Spy
    private GradeMapper gradeMapper = Mappers.getMapper(GradeMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("Database save grade and gradeSystem")
    @Test
    void givenGradeSystemAndGradeEntityWhenSaveGradeSystemThenReturnSavedEntityWithRightDetails() {
        GradeSystem gradeSystemEntity = getGradeSystemTestEntity();
        List<Grade> gradeEntities = getGradesTestEntities();
        List<Grade> createdGrades = new ArrayList<>();

        for(Grade grade : gradeEntities) {
            Grade createdGrade = testEntityManager.persist(grade);
            createdGrades.add(createdGrade);
        }

        gradeSystemEntity.setGrades(createdGrades);

        GradeSystem createdGradeSystem = testEntityManager.persistAndFlush(gradeSystemEntity);

        assertThat(createdGradeSystem.getId()).isNotNull();
        assertThat(createdGradeSystem.getGrades().size()).isEqualTo(2);
        Assertions.assertThat(createdGradeSystem.getGrades().get(0)).usingRecursiveComparison().isEqualTo(gradeEntities.get(0));
        Assertions.assertThat(createdGradeSystem.getGrades().get(1)).usingRecursiveComparison().isEqualTo(gradeEntities.get(1));
    }

    @DisplayName("Create grade system with existent Grade and new Grade")
    @Test
    void givenValidGradeSystemRequestDtoWhenCreateGradeSystemThenCreateGradeSystemAndReturnGradeSystemDto() {
        GradeSystemRequestDto gradeSystemToCreate = getGradeSystemRequestDto();
        Grade newGradeEntity = Grade.builder().id(2l).name("Grade B").description("New Grade").maxValue(0.7).build();

        given(gradeRepository.save(gradeMapper.requestDtoToEntity(gradeSystemToCreate.getGrades().get(1)))).willReturn(newGradeEntity);

        GradeSystemDto createdGradeSystem = gradeSystemService.createGradeSystem(gradeSystemToCreate);
        System.out.println(createdGradeSystem);
        assertThat(createdGradeSystem.isActive()).isTrue();
        assertThat(createdGradeSystem.getId()).isNotNull();
        assertThat(gradeSystemToCreate.getGrades().get(0)).isEqualTo(createdGradeSystem.getGrades().get(0));
        assertThat(gradeSystemToCreate.getGrades().get(1)).usingRecursiveComparison().ignoringFields("id").isEqualTo(createdGradeSystem.getGrades().get(1));
        assertThat(createdGradeSystem.getGrades().get(1).getId()).isNotNull();
    }

    @DisplayName("Create grade system with grades values bigger than GradeSystem max value should throw Exception")
    @Test
    void givenGradesMaxValuesSumBiggerThanGradeSystemMaxValueWhenCreateGradeSystemThenThrowsGradeException() {
        GradeSystemRequestDto gradeSystemToCreate = getGradeSystemRequestDto();
        gradeSystemToCreate.getGrades().get(1).setId(2l);
        gradeSystemToCreate.getGrades().get(1).setMaxValue(1.0);

        assertThatThrownBy(
                () -> gradeSystemService.createGradeSystem(gradeSystemToCreate)).isInstanceOf(GradeException.class)
                .hasMessage(GRADE_VALUES_BIGGER_THEN_GRADE_SYSTEM_MAX_VALUE.getMessageKey());
    }

    @DisplayName("Get valid grade system")
    @Test
    void givenValidIdWhenGetGradeSystemThenReturnsGradeSystemDto() {
        GradeSystem foundGradeSystem = getGradeSystemTestEntity();
        foundGradeSystem.setId(1l);

        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.of(foundGradeSystem));

        GradeSystemDto returnedGradeSystemDto = gradeSystemService.getGradeSystem(1l);

        assertThat(returnedGradeSystemDto).usingRecursiveComparison().isEqualTo(foundGradeSystem);

    }

    @DisplayName("Get invalid grade system should throw Resource Not Found Exception")
    @Test
    void givenInvalidGradeSystemIdWhenGetGradeSystemThenThrowsResourceNotFoundException() {
        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> gradeSystemService.getGradeSystem(1l)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(GRADE_SYSTEM_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get all grade systems")
    @Test
    void givenValidGradeSystemsWhenGetGradeSystemThenReturnsListOfGradeSystemDto() {
        List<GradeSystem> gradeEntities = getGradeSystemEntities();

        given(gradeSystemRepository.findAll()).willReturn(gradeEntities);

        List<GradeSystemDto> foundGradeSystems = gradeSystemService.getAllGradeSystem();
        assertThat(foundGradeSystems).usingRecursiveComparison().isEqualTo(gradeEntities);
        assertThat(foundGradeSystems.size()).isEqualTo(3);
    }

    @DisplayName("Update grade system")
    @Test
    void givenValidGradeSystemRequestDtoWhenUpdateGradeSystemThenReturnsGradeSystemDto() {
        GradeSystem gradeSystemEntity = getGradeSystemTestEntity();
        List<Grade> gradeEntities = getGradesTestEntities();
        List<Grade> createdGrades = new ArrayList<>();

        for(Grade grade : gradeEntities) {
            Grade createdGrade = testEntityManager.persist(grade);
            createdGrades.add(createdGrade);
        }

        gradeSystemEntity.setGrades(createdGrades);

        GradeSystem createdGradeSystem = testEntityManager.persistAndFlush(gradeSystemEntity);

        GradeSystemRequestDto gradeSystemToUpdate = GradeSystemRequestDto.builder().name("Updated Grade System").build();

        GradeSystem expectedUpdatedGradeSystem = createdGradeSystem;
        expectedUpdatedGradeSystem.setName(gradeSystemToUpdate.getName());

        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.of(createdGradeSystem));
        given(gradeSystemRepository.save(createdGradeSystem)).willReturn(expectedUpdatedGradeSystem);

        GradeSystemDto updatedGradeSystem = gradeSystemService.updateGradeSystem(1l, gradeSystemToUpdate);
        assertThat(updatedGradeSystem.getName()).isEqualTo(gradeSystemToUpdate.getName());
        verify(gradeSystemRepository).save(any(GradeSystem.class));
        verifyNoInteractions(gradeRepository);
    }

    @DisplayName("Update grade system when request has grades")
    @Test
    void givenValidGradeSystemRequestDtoWithGradesWhenUpdateGradeSystemThenReturnsGradeSystemDto() {
        GradeSystem gradeSystemEntity = getGradeSystemTestEntity();
        List<Grade> gradeEntities = getGradesTestEntities();
        List<Grade> createdGrades = new ArrayList<>();

        for(Grade grade : gradeEntities) {
            Grade createdGrade = testEntityManager.persist(grade);
            createdGrades.add(createdGrade);
        }

        gradeSystemEntity.setGrades(createdGrades);

        GradeSystem createdGradeSystem = testEntityManager.persistAndFlush(gradeSystemEntity);

        GradeDto gradeToUpdateA = GradeDto.builder().id(gradeSystemEntity.getGrades().get(0).getId()).name(gradeSystemEntity.getGrades().get(0).getName()).maxValue(gradeSystemEntity.getGrades().get(0).getMaxValue()).description(gradeSystemEntity.getGrades().get(0).getDescription()).build();
        GradeDto gradeToUpdateB = GradeDto.builder().id(gradeSystemEntity.getGrades().get(1).getId()).name("Grade Updated Name").maxValue(0.1).description(gradeSystemEntity.getGrades().get(0).getDescription()).build();
        GradeDto gradeToUpdateC = GradeDto.builder().name("New Grade").maxValue(0.1).description("new grade description").build();

        GradeSystemRequestDto gradeSystemToUpdate = GradeSystemRequestDto.builder().name("Updated Grade System")
                .grades(Arrays.asList(
                        gradeToUpdateA,
                        gradeToUpdateB,
                        gradeToUpdateC
                ))
                .build();

        GradeSystem expectedUpdatedGradeSystem = createdGradeSystem;
        expectedUpdatedGradeSystem.setName(gradeSystemToUpdate.getName());

        Grade createdGradeB = Grade.builder().id(gradeSystemEntity.getGrades().get(1).getId()).name("Grade Updated Name").maxValue(0.1).description(gradeSystemEntity.getGrades().get(0).getDescription()).build();
        Grade createdGradeC = Grade.builder().id(3l).name("New Grade").maxValue(0.1).description("new grade description").build();

        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.of(createdGradeSystem));
        given(gradeRepository.save(gradeMapper.requestDtoToEntity(gradeSystemToUpdate.getGrades().get(1)))).willReturn(createdGradeB);
        given(gradeRepository.save(gradeMapper.requestDtoToEntity(gradeSystemToUpdate.getGrades().get(2)))).willReturn(createdGradeC);
        given(gradeSystemRepository.save(createdGradeSystem)).willReturn(expectedUpdatedGradeSystem);

        GradeSystemDto updatedGradeSystem = gradeSystemService.updateGradeSystem(1l, gradeSystemToUpdate);
        assertThat(updatedGradeSystem.getName()).isEqualTo(gradeSystemToUpdate.getName());
        assertThat(updatedGradeSystem.getGrades()).usingRecursiveComparison().ignoringFields("id").isEqualTo(gradeSystemToUpdate.getGrades());

        verify(gradeSystemRepository).save(any(GradeSystem.class));
        verify(gradeRepository,times(2)).save(any(Grade.class));

    }

    @DisplayName("Delete grade system")
    @Test
    void givenValidIdWhenDeleteGradeSystemThenReturnsGradeSystemDtoWithActiveFalse() {
        GradeSystem foundGradeSystem = getGradeSystemEntity();

        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.of(foundGradeSystem));

        GradeSystemDto returnedGradeSystem = gradeSystemService.deleteGradeSystem(1l);

        assertThat(returnedGradeSystem.isActive()).isFalse();
    }

    @DisplayName("Delete grade system")
    @Test
    void givenInvalidIdWhenDeleteGradeSystemThenThrowsResourceNotFoundException() {
        given(gradeSystemRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> gradeSystemService.deleteGradeSystem(1l)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(GRADE_SYSTEM_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get all active grade systems")
    @Test
    void givenGradeSystemsWhenGetActiveGradeSystemThenReturnsActiveGradeSystemsOnly() {
        testEntityManager.persist(GradeSystem.builder().active(true).build());
        testEntityManager.persist(GradeSystem.builder().active(false).build());
        testEntityManager.persist(GradeSystem.builder().active(true).build());

        List<GradeSystemDto> returnedGradeSystems = gradeSystemService.getAllActiveGradeSystems();

        for(GradeSystemDto gradeSystemDto : returnedGradeSystems) {
            assertThat(gradeSystemDto.isActive()).isTrue();
        }
    }


}
