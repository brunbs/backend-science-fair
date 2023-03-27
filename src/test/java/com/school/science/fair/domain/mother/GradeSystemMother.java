package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateGradeRequest;
import com.school.science.fair.domain.CreateGradeSystemRequest;
import com.school.science.fair.domain.dto.GradeDto;
import com.school.science.fair.domain.dto.GradeSystemDto;
import com.school.science.fair.domain.dto.GradeSystemRequestDto;
import com.school.science.fair.domain.entity.Grade;
import com.school.science.fair.domain.entity.GradeSystem;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GradeSystemMother {

    private static Random randomNumber = new Random();

    public static GradeSystem getGradeSystemTestEntity() {
        return GradeSystem.builder()
                .name("Grade System A")
                .description("Grade System Description")
                .active(true)
                .maxValue(1.0)
                .build();
    }

    public static GradeSystem getGradeSystemEntity() {
        return GradeSystem.builder()
                .id(1l)
                .name("Grade System A")
                .description("Grade System Description")
                .active(true)
                .maxValue(1.0)
                .build();
    }

    public static List<Grade> getGradesTestEntities() {
        return Arrays.asList(
                Grade.builder().name("Grade A").description("Grade A Description").maxValue(0.5).build(),
                Grade.builder().name("Grade B").description("Grade B Description").maxValue(0.1).build()
        );
    }

    public static List<Grade> getGradesEntities() {
        return Arrays.asList(
                Grade.builder().id(1l).name("Grade A").description("Grade A Description").maxValue(0.3).build(),
                Grade.builder().id(2l).name("Grade B").description("Grade B Description").maxValue(0.2).build()
        );
    }

    public static GradeSystemRequestDto getGradeSystemRequestDto() {
        return GradeSystemRequestDto.builder()
                .name("Grade System A")
                .description("Grade System Description")
                .maxValue(1.0)
                .grades(getCreateGradeDto())
                .build();
    }

    public static List<GradeDto> getCreateGradeDto() {
        return Arrays.asList(GradeDto.builder()
                        .id(1l)
                        .name("Grade A")
                        .description("Existent grade")
                        .maxValue(0.3)
                        .build(),
                GradeDto.builder()
                        .name("Grade B")
                        .description("New Grade")
                        .maxValue(0.7).build());
    }

    public static List<GradeSystem> getGradeSystemEntities() {
        GradeSystem gradeSystemA = getARandomGradeSystem();
        gradeSystemA.setGrades(Arrays.asList(getARandomGradeEntityWithId(), getARandomGradeEntityWithId()));

        GradeSystem gradeSystemB = getARandomGradeSystem();
        gradeSystemB.setGrades(Arrays.asList(getARandomGradeEntityWithId(), getARandomGradeEntityWithId()));

        GradeSystem gradeSystemC = getARandomGradeSystem();
        gradeSystemC.setGrades(Arrays.asList(getARandomGradeEntityWithId(), getARandomGradeEntityWithId()));

        return Arrays.asList(gradeSystemA, gradeSystemB, gradeSystemC);
    }

    public static GradeSystem getARandomGradeSystem() {
        return GradeSystem.builder()
                .id(randomNumber.nextLong())
                .name(RandomStringUtils.random(10))
                .description(RandomStringUtils.random(50))
                .maxValue(1.0)
                .build();
    }

    public static Grade getARandomGradeEntityWithId() {
        return Grade.builder()
                .id(randomNumber.nextLong())
                .name(RandomStringUtils.random(10))
                .description(RandomStringUtils.random(50))
                .maxValue(0.1)
                .build();
    }

    public static CreateGradeSystemRequest getCreateGradeSystemRequest() {
        return CreateGradeSystemRequest.builder()
                .name("Grade System A")
                .description("Grade System Description")
                .maxValue(1.0)
                .grades(Arrays.asList(
                        CreateGradeRequest.builder()
                                .name("Grade A")
                                .description("Grade A Description")
                                .maxValue(0.3)
                                .build(),
                        CreateGradeRequest.builder()
                                .name("Grade B")
                                .description("Grade B Description")
                                .maxValue(0.2)
                                .build()
                ))
                .build();
    }

    public static GradeSystemDto getGradeSystemDto() {
        return GradeSystemDto.builder()
                .id(1l)
                .name("Grade System A")
                .description("Grade System Description")
                .maxValue(1.0)
                .active(true)
                .grades(Arrays.asList(
                        GradeDto.builder()
                                .id(1l)
                                .name("Grade A")
                                .description("Grade A Description")
                                .maxValue(0.3)
                                .build(),
                        GradeDto.builder()
                                .id(2l)
                                .name("Grade B")
                                .description("Grade B Description")
                                .maxValue(0.2)
                                .build()
                ))
                .build();
    }

    public static List<GradeSystemDto> getGradeSystemDtos() {
        GradeSystemDto gradeSystemA = getGradeSystemDto();
        GradeSystemDto gradeSystemB = getGradeSystemDto();
        gradeSystemB.setId(2l);
        gradeSystemB.getGrades().get(0).setId(3l);
        gradeSystemB.setActive(false);
        GradeSystemDto gradeSystemC = getGradeSystemDto();
        gradeSystemC.setId(3l);
        gradeSystemB.getGrades().get(1).setId(4l);
        return Arrays.asList(gradeSystemA, gradeSystemB, gradeSystemC);
    }

}
