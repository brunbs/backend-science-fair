package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateScienceFairRequest;
import com.school.science.fair.domain.dto.ScienceFairDto;
import com.school.science.fair.domain.dto.ScienceFairRequestDto;
import com.school.science.fair.domain.entity.ScienceFair;

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

}
