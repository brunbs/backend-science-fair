package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.dto.UserProjectDto;

import java.util.List;

public class UserProjectMother {

    public static List<UserProjectDto> getStudentsUserProjectDtoList() {
        return List.of(
                UserProjectDto.builder().registration(1l).email("studentA@email.com").name("Student A").build(),
                UserProjectDto.builder().registration(2l).email("studentB@email.com").name("Student B").build()
        );
    }

    public static UserProjectDto getTeacherUserProjectDto() {
        return UserProjectDto.builder().registration(3l).email("teacher@email.com").name("Teacher A").build();
    }

}
