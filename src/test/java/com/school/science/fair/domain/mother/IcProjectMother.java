package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.CreateProjectRequest;
import com.school.science.fair.domain.dto.CreateProjectDto;
import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.entity.IcProject;

import java.util.List;

import static com.school.science.fair.domain.mother.AreaOfKnowledgeMother.getATopicDto;
import static com.school.science.fair.domain.mother.AreaOfKnowledgeMother.getTopicEntity;
import static com.school.science.fair.domain.mother.ProjectGradeDtoMother.getProjectGradeDtos;
import static com.school.science.fair.domain.mother.ScienceFairMother.getScienceFairEntity;
import static com.school.science.fair.domain.mother.UserProjectMother.getStudentsUserProjectDtoList;
import static com.school.science.fair.domain.mother.UserProjectMother.getTeacherUserProjectDto;

public class IcProjectMother {

    public static CreateProjectDto getCreateProjectDto() {
        return CreateProjectDto.builder()
                .title("Project A")
                .description("Project A Description")
                .topicId(1l)
                .studentsRegistrations(List.of(1l, 2l))
                .build();
    }

    public static IcProject getIcProjectEntity() {
        return IcProject.builder()
                .id(1l)
                .scienceFair(getScienceFairEntity())
                .topic(getTopicEntity())
                .title("Project A")
                .description("Project A Description")
                .build();
    }

    public static ProjectDto getProjectDto() {
        return ProjectDto.builder()
                .id(1l)
                .description("Project A Description")
                .title("Project A")
                .grades(getProjectGradeDtos())
                .students(getStudentsUserProjectDtoList())
                .teacher(getTeacherUserProjectDto())
                .topic(getATopicDto())
                .build();
    }

    public static CreateProjectRequest getCreateProjectRequest() {
        return CreateProjectRequest.builder()
                .title("Project A")
                .description("Project A Description")
                .studentsRegistrations(List.of(1l, 2l))
                .topicId(1l)
                .build();
    }

}
