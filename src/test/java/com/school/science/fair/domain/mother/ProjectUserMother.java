package com.school.science.fair.domain.mother;

import com.school.science.fair.domain.dto.ProjectUserDto;
import com.school.science.fair.domain.entity.ProjectUser;
import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.UserTypeEnum;

import java.util.List;

import static com.school.science.fair.domain.mother.IcProjectMother.getIcProjectEntity;
import static com.school.science.fair.domain.mother.UsersMother.getStudentEntity;
import static com.school.science.fair.domain.mother.UsersMother.getTeacherEntity;

public class ProjectUserMother {

    public static List<ProjectUserDto> getStudentsUserProjectDtoList() {
        return List.of(
                ProjectUserDto.builder().registration(1l).email("studentA@email.com").name("Student A").role(UserTypeEnum.STUDENT).build(),
                ProjectUserDto.builder().registration(2l).email("studentB@email.com").name("Student B").role(UserTypeEnum.STUDENT).build()
        );
    }

    public static ProjectUserDto getTeacherUserProjectDto() {
        return ProjectUserDto.builder().registration(3l).email("teacher@email.com").name("Teacher A").build();
    }

    public static List<ProjectUserDto> getProjectUserDtos() {
        return List.of(
                ProjectUserDto.builder().registration(1l).email("studentA@email.com").name("Student A").role(UserTypeEnum.STUDENT).build(),
                ProjectUserDto.builder().registration(2l).email("studentB@email.com").name("Student B").role(UserTypeEnum.STUDENT).build(),
                ProjectUserDto.builder().registration(3l).email("teacher@email.com").name("Teacher").role(UserTypeEnum.TEACHER).build()
        );
    }

    public static List<ProjectUser> getProjectUsers() {
        Users studentA = getStudentEntity();
        Users studentB = getStudentEntity();
        studentB.setName("Student B");
        studentB.setRegistration(2l);
        studentB.setEmail("studentB@email.com");
        Users teacher = getTeacherEntity();

        return List.of(
                ProjectUser.builder().icProject(getIcProjectEntity()).id(1l).users(studentA).role(studentA.getUserType()).build(),
                ProjectUser.builder().icProject(getIcProjectEntity()).id(2l).users(studentB).role(studentB.getUserType()).build(),
                ProjectUser.builder().icProject(getIcProjectEntity()).id(3l).users(teacher).role(teacher.getUserType()).build()
        );
    }

}
