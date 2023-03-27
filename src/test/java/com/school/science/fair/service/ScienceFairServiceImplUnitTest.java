package com.school.science.fair.service;

import com.school.science.fair.domain.dto.*;
import com.school.science.fair.domain.entity.GradeSystem;
import com.school.science.fair.domain.entity.ScienceFair;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.domain.mapper.ScienceFairMapper;
import com.school.science.fair.repository.ScienceFairRepository;
import com.school.science.fair.service.impl.ScienceFairServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static com.school.science.fair.domain.enumeration.ExceptionMessage.GRADE_SYSTEM_NOT_FOUND;
import static com.school.science.fair.domain.enumeration.ExceptionMessage.SCIENCE_FAIR_NOT_FOUND;
import static com.school.science.fair.domain.mother.GradeSystemMother.*;
import static com.school.science.fair.domain.mother.ScienceFairMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@DataJpaTest
public class ScienceFairServiceImplUnitTest {

    @SpyBean
    private ScienceFairRepository scienceFairRepository;

    @InjectMocks
    private ScienceFairServiceImpl scienceFairService;

    @Spy
    private ScienceFairMapper scienceFairMapper = Mappers.getMapper(ScienceFairMapper.class);

    @Spy
    private GradeSystemMapper gradeSystemMapper = Mappers.getMapper(GradeSystemMapper.class);

    @Spy
    private GradeMapper gradeMapper = Mappers.getMapper(GradeMapper.class);

    @Autowired
    private TestEntityManager testEntityManager;

    @Mock
    private GradeSystemService gradeSystemService;

    @DisplayName("Create Science Fair")
    @Test
    void givenValidScienceFairRequestDtoWhenCreateGradeSystemThenReturnCreatedScienceFair() {

        ScienceFairRequestDto createScienceFairRequestDto = getCreateScienceFairRequestDto();
        ScienceFair createdScienceFair = getScienceFairEntity();
        createdScienceFair.setActive(true);
        createdScienceFair.getGradeSystem().setGrades(getGradesEntities());
        ScienceFair scienceFairToCreateEntity = getScienceFairEntity();
        scienceFairToCreateEntity.setId(null);
        scienceFairToCreateEntity.setGradeSystem(null);
        scienceFairToCreateEntity.setActive(true);
        GradeSystemDto foundGradeSystem = getGradeSystemDto();

        given(gradeSystemService.getGradeSystem(anyLong())).willReturn(foundGradeSystem);
        given(scienceFairMapper.requestDtoToEntity(createScienceFairRequestDto)).willReturn(scienceFairToCreateEntity);
        given(scienceFairRepository.save(scienceFairToCreateEntity)).willReturn(createdScienceFair);
        ScienceFairDto createdScienceFairDto = scienceFairService.createScienceFair(createScienceFairRequestDto);

        assertThat(createdScienceFairDto.getId()).isNotNull();
        assertThat(createdScienceFairDto).usingRecursiveComparison().ignoringFields("id", "gradeSystemId", "gradeSystem", "active").isEqualTo(createScienceFairRequestDto);
        assertThat(createdScienceFairDto.getGradeSystem()).usingRecursiveComparison().isEqualTo(foundGradeSystem);
    }

    @DisplayName("Create Science Fair with invalid grade system id throws ResourceNotFoundException")
    @Test
    void givenInvalidGradeSystemIdWhenCreateScienceFairThenThrowsResourceNotFoundExceptionWithCorrectMessage() {
        ScienceFairRequestDto createScienceFairRequestDto = getCreateScienceFairRequestDto();
        given(gradeSystemService.getGradeSystem(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, ExceptionMessage.GRADE_SYSTEM_NOT_FOUND));

        assertThatThrownBy(
                () -> scienceFairService.createScienceFair(createScienceFairRequestDto)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(GRADE_SYSTEM_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get Science Fair")
    @Test
    void givenValidIdWhenGetScienceFairThenReturnsScienceFairDto() {
        ScienceFair foundScienceFairFromDatabase = getScienceFairEntity();

        given(scienceFairRepository.findById(anyLong())).willReturn(Optional.of(foundScienceFairFromDatabase));

        ScienceFairDto returendScienceFair = scienceFairService.getScienceFair(1l);

        assertThat(returendScienceFair).usingRecursiveComparison().isEqualTo(foundScienceFairFromDatabase);
    }

    @DisplayName("Get Science Fair with invalid id throws ResourceNotFoundException")
    @Test
    void givenInvalidIdWhenGetScienceFairThenThrowsResourceNotFoundException() {
        given(scienceFairRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> scienceFairService.getScienceFair(1l))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(ExceptionMessage.SCIENCE_FAIR_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Update Science Fair name but not the description, editionYear and GradeSystem")
    @Test
    void givenValidIdAndUpdatedNameInUpdateScienceFairRequestWhenUpdateScienceFairThenReturnsUpdatedScienceFair() {
        ScienceFair scienceFairEntity = getScienceFairEntity();
        scienceFairEntity.setId(null);
        UpdateScienceFairDto updateScienceFairDto = UpdateScienceFairDto.builder().name("Updated Name").build();
        ScienceFair updatedScienceFair = getScienceFairEntity();
        updatedScienceFair.setName(updateScienceFairDto.getName());

        GradeSystem gradeSystem = testEntityManager.persistAndFlush(getGradeSystemTestEntity());
        scienceFairEntity.setGradeSystem(gradeSystem);
        ScienceFair scienceFairFromDatabase = testEntityManager.persistAndFlush(scienceFairEntity);

        given(scienceFairRepository.findById(anyLong())).willReturn(Optional.of(scienceFairFromDatabase));
        given(scienceFairRepository.save(scienceFairFromDatabase)).willReturn(updatedScienceFair);

        ScienceFairDto returnedUpdatedScienceFair = scienceFairService.updateScienceFair(1l, updateScienceFairDto);

        assertThat(returnedUpdatedScienceFair).usingRecursiveComparison().ignoringFields("name", "gradeSystem.id", "id").isEqualTo(scienceFairFromDatabase);
        assertThat(returnedUpdatedScienceFair.getName()).isEqualTo(updateScienceFairDto.getName());
    }

    @DisplayName("Update Science Fair description but not the name, editionYear and GradeSystem")
    @Test
    void givenValidIdAndUpdatedDescriptionInUpdateScienceFairRequestWhenUpdateScienceFairThenReturnsUpdatedScienceFair() {
        ScienceFair scienceFairEntity = getScienceFairEntity();
        scienceFairEntity.setId(null);
        UpdateScienceFairDto updateScienceFairDto = UpdateScienceFairDto.builder().description("Updated Description").build();

        GradeSystem gradeSystem = testEntityManager.persistAndFlush(getGradeSystemTestEntity());
        scienceFairEntity.setGradeSystem(gradeSystem);
        ScienceFair scienceFairFromDatabase = testEntityManager.persistAndFlush(scienceFairEntity);

        ScienceFair updatedScienceFair = getScienceFairEntity();
        updatedScienceFair.setDescription(updateScienceFairDto.getDescription());

        given(scienceFairRepository.findById(anyLong())).willReturn(Optional.of(scienceFairFromDatabase));
        given(scienceFairRepository.save(scienceFairFromDatabase)).willReturn(updatedScienceFair);

        ScienceFairDto returnedUpdatedScienceFair = scienceFairService.updateScienceFair(1l, updateScienceFairDto);

        assertThat(returnedUpdatedScienceFair).usingRecursiveComparison().ignoringFields("description", "gradeSystem.id", "id").isEqualTo(scienceFairFromDatabase);
        assertThat(returnedUpdatedScienceFair.getDescription()).isEqualTo(updateScienceFairDto.getDescription());
    }

    @DisplayName("Update Science Fair Throws Exception when invalid science fair id")
    @Test
    void givenInvalidIdWhenUpdateScienceFairThenThrowsResourceNotFoundException() {
        given(scienceFairRepository.findById(anyLong())).willThrow(new ResourceNotFoundException(HttpStatus.NOT_FOUND, SCIENCE_FAIR_NOT_FOUND));

        assertThatThrownBy(
                () -> scienceFairService.updateScienceFair(1l, mock(UpdateScienceFairDto.class)))
                .isInstanceOf(ResourceNotFoundException.class).hasMessage(SCIENCE_FAIR_NOT_FOUND.getMessageKey());
    }

    @DisplayName("Get all science fairs")
    @Test
    void givenValidScienceFairsWhenGetAllScienceFairsThenReturnsScienceFairListDto() {
        List<ScienceFair> foundEntities = getScienceFairListEntity();

        given(scienceFairRepository.findAll()).willReturn(foundEntities);

        List<ScienceFairDto> foundScienceFairs = scienceFairService.getAllScienceFairs();

        assertThat(foundScienceFairs).usingRecursiveComparison().isEqualTo(foundEntities);
    }

}
