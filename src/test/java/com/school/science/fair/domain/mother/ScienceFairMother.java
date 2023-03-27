package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.entity.ScienceFair;

import java.util.Arrays;
import java.util.List;

import static com.school.science.fair.domain.mother.GradeSystemMother.getGradeSystemDto;
import static com.school.science.fair.domain.mother.GradeSystemMother.getGradeSystemEntity;

public class ScienceFairMother {

    public static ScienceFairRequestDto getCreateScienceFairRequestDto() {
        return ScienceFairRequestDto.builder()
                .name("Science Fair A")
                .description("Science Fair Description")
                .gradeSystemId(1l)
                .editionYear(2023)
                .build();
    }

    public static ScienceFair getScienceFairEntity() {
        return ScienceFair.builder()
                .id(1l)
                .name("Science Fair A")
                .description("Science Fair Description")
                .active(true)
                .gradeSystem(getGradeSystemEntity())
                .editionYear(2023)
                .build();
    }

    public static CreateScienceFairRequest getCreateScienceFairRequest() {
        return CreateScienceFairRequest.builder()
                .name("Science Fair A")
                .description("Science Fair Description")
                .editionYear(2023)
                .gradeSystemId(1l)
                .build();
    }

    public static ScienceFairDto getScienceFairDto() {
        return ScienceFairDto.builder()
                .id(1l)
                .name("Science Fair A")
                .description("Science Fair Description")
                .editionYear(2023)
                .gradeSystem(getGradeSystemDto())
                .build();
    }

    public static List<ScienceFair> getScienceFairListEntity() {
        ScienceFair entityA = getScienceFairEntity();
        ScienceFair entityB = getScienceFairEntity();
        entityB.setName("Science Fair B");
        entityB.setActive(false);
        ScienceFair entityC = getScienceFairEntity();
        entityB.getGradeSystem().setName("Grade System for Science Fair B");
        entityC.setName("Science Fair C");
        entityC.getGradeSystem().setName("Grade System for Science Fair C");
        return Arrays.asList(entityA, entityB, entityC);
    }

    public static List<ScienceFairDto> getScienceFairDtoList() {
        ScienceFairDto scienceFairA = getScienceFairDto();
        ScienceFairDto scienceFairB = getScienceFairDto();
        scienceFairB.setName("Science Fair B");
        scienceFairB.setActive(false);
        ScienceFairDto scienceFairC = getScienceFairDto();
        scienceFairB.getGradeSystem().setName("Grade System for Science Fair B");
        scienceFairC.setName("Science Fair C");
        scienceFairC.getGradeSystem().setName("Grade System for Science Fair C");
        return Arrays.asList(scienceFairA, scienceFairB, scienceFairC);
    }

}
