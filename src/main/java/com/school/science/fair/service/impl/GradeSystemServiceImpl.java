package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.entity.Grade;
import com.school.science.fair.domain.entity.GradeSystem;
import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.exception.GradeException;
import com.school.science.fair.domain.exception.ResourceNotFoundException;
import com.school.science.fair.domain.mapper.GradeMapper;
import com.school.science.fair.domain.mapper.GradeSystemMapper;
import com.school.science.fair.repository.GradeRepository;
import com.school.science.fair.repository.GradeSystemRepository;
import com.school.science.fair.service.GradeSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeSystemServiceImpl implements GradeSystemService {

    @Autowired
    private GradeSystemRepository gradeSystemRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private GradeSystemMapper gradeSystemMapper;

    @Autowired
    private GradeMapper gradeMapper;


    @Override
    public GradeSystemDto createGradeSystem(GradeSystemRequestDto gradeSystemRequestDto) {
        GradeSystem gradeSystemToCreate = gradeSystemMapper.requestDtoToEntity(gradeSystemRequestDto);
        checkIfSumOfGradesIsLessOrEqualGradeSystemMaxValue(gradeSystemToCreate.getMaxValue(), gradeSystemToCreate.getGrades());
        List<Grade> grades = createGradesAndReturnListOfGradesEntity(gradeSystemToCreate.getGrades());
        gradeSystemToCreate.setGrades(grades);
        gradeSystemToCreate.setActive(true);
        GradeSystem createdGradeSystem = gradeSystemRepository.save(gradeSystemToCreate);
        return gradeSystemMapper.entityToDto(createdGradeSystem);
    }

    @Override
    public GradeSystemDto getGradeSystem(Long id) {
        GradeSystem foundGradeSystem = getGradeSystemOrThrowException(id);
        return gradeSystemMapper.entityToDto(foundGradeSystem);
    }

    @Override
    public List<GradeSystemDto> getAllGradeSystem() {
        List<GradeSystem> foundGradeSystems = gradeSystemRepository.findAll();
        return gradeSystemMapper.listEntityToListDto(foundGradeSystems);
    }

    @Override
    @Transactional
    public GradeSystemDto updateGradeSystem(Long id, GradeSystemRequestDto gradeSystemRequestDto) {
        GradeSystem gradeSystemToUpdate = getGradeSystemOrThrowException(id);
        gradeSystemMapper.updateModelFromDto(gradeSystemRequestDto, gradeSystemToUpdate);
        if(gradeSystemRequestDto.getGrades() != null) {
            List<Grade> gradesToSave = updateGradesAndReturnListOfGradeEntity(gradeSystemToUpdate.getGrades(), gradeSystemMapper.listDtoToListEntity(gradeSystemRequestDto.getGrades()));
            gradeSystemToUpdate.setGrades(gradesToSave);
        }
        checkIfSumOfGradesIsLessOrEqualGradeSystemMaxValue(gradeSystemToUpdate.getMaxValue(), gradeSystemToUpdate.getGrades());
        GradeSystem updatedGradeSystem = gradeSystemRepository.save(gradeSystemToUpdate);
        return gradeSystemMapper.entityToDto(updatedGradeSystem);
    }

    @Override
    public GradeSystemDto deleteGradeSystem(Long id) {
        GradeSystem gradeSystemToDelete = getGradeSystemOrThrowException(id);
        gradeSystemToDelete.setActive(false);
        GradeSystem deletedGradeSystem = gradeSystemRepository.save(gradeSystemToDelete);
        return gradeSystemMapper.entityToDto(deletedGradeSystem);
    }

    @Override
    public List<GradeSystemDto> getAllActiveGradeSystems() {
        List<GradeSystem> foundGradeSystems = gradeSystemRepository.findAllByActiveTrue();
        return gradeSystemMapper.listEntityToListDto(foundGradeSystems);
    }

    private GradeSystem getGradeSystemOrThrowException(Long id) {
        Optional<GradeSystem> foundGradeSystem = gradeSystemRepository.findById(id);
        if(foundGradeSystem.isPresent()) {
            return foundGradeSystem.get();
        }
        throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, ExceptionMessage.GRADE_SYSTEM_NOT_FOUND);
    }

    private List<Grade> createGradesAndReturnListOfGradesEntity(List<Grade> gradesToCreate) {
        List<Grade> gradesToReturn = new ArrayList<>();
        for(Grade grade : gradesToCreate) {
            if(grade.getId() == null) {
                Grade createdGrade = gradeRepository.save(grade);
                gradesToReturn.add(createdGrade);
                continue;
            }
            gradesToReturn.add(grade);
        }
        return gradesToReturn;
    }

    private List<Grade> updateGradesAndReturnListOfGradeEntity(List<Grade> grades, List<Grade> gradesToCreate) {
        List<Grade> gradesToReturn = gradesToCreate.stream().filter(grades::contains).collect(Collectors.toList());
        List<Grade> gradesToSave = gradesToCreate.stream().filter(g -> !grades.contains(g)).collect(Collectors.toList());
        for(Grade grade : gradesToSave) {
            Grade savedGrade = gradeRepository.save(grade);
            gradesToReturn.add(savedGrade);
        }
        return gradesToReturn;
    }

    private void checkIfSumOfGradesIsLessOrEqualGradeSystemMaxValue(Double maxValue, List<Grade> grades) {
        Double sumOfGrades = 0.0;
        for(Grade grade : grades) {
            sumOfGrades += grade.getMaxValue();
        }
        if (sumOfGrades > maxValue) {
            throw new GradeException(HttpStatus.BAD_REQUEST, ExceptionMessage.GRADE_VALUES_BIGGER_THEN_GRADE_SYSTEM_MAX_VALUE);
        }
    }

}
