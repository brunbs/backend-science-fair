package com.school.science.fair.service;

import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.UserDto;
import com.school.science.fair.domain.dto.ProjectUserDto;
import com.school.science.fair.domain.entity.ProjectUser;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.repository.ProjectUserRepository;
import com.school.science.fair.service.impl.ProjectUserServiceImpl;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.school.science.fair.domain.mother.IcProjectMother.getProjectDto;
import static com.school.science.fair.domain.mother.ProjectUserMother.getProjectUsers;
import static com.school.science.fair.domain.mother.UsersMother.getUserDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class ProjectUserServiceImplUnitTest {

    @InjectMocks
    private ProjectUserServiceImpl projectUserService;

    @Spy
    private IcProjectMapper icProjectMapper = Mappers.getMapper(IcProjectMapper.class);

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserService userService;

    @Mock
    private ProjectUserRepository projectUserRepository;

    @DisplayName("Insert Students into Project")
    @Test
    void givenValidStudentsRegistrationsAndProjectDtoWhenInsertUsersInProjectThenReturnsUserProjectDtos() {
        List<Long> studentsRegistrations = List.of(1l, 2l);
        ProjectDto projectDto = getProjectDto();
        UserDto studentA = getUserDto();
        UserDto studentB = getUserDto();
        studentB.setName("Student B");
        studentB.setEmail("studentB@gmail.com");
        studentB.setRegistration(2l);

        given(userService.getUser(1l, UserTypeEnum.STUDENT)).willReturn(studentA);
        given(userService.getUser(2l, UserTypeEnum.STUDENT)).willReturn(studentB);
        given(projectUserRepository.save(any(ProjectUser.class))).willReturn(mock(ProjectUser.class));

        List<ProjectUserDto> returnedProjectUserDtos = projectUserService.insertUsersInProject(studentsRegistrations, projectDto);

        assertThat(returnedProjectUserDtos.get(0)).usingRecursiveComparison().ignoringFields("password", "active", "userType", "role").isEqualTo(studentA);
        assertThat(returnedProjectUserDtos.get(1)).usingRecursiveComparison().ignoringFields("password", "active", "userType", "role").isEqualTo(studentB);
    }

    @DisplayName("Get a project users")
    @Test
    void givenValidProjectWhenGetProjectUsersThenReturnListWithProjectUserDto() {
        List<ProjectUser> projectUsers = getProjectUsers();
        given(projectUserRepository.findAllByIcProjectId(anyLong())).willReturn(projectUsers);

        List<ProjectUserDto> projectUserDtos = projectUserService.getProjectUsers(1l);
        assertThat(projectUserDtos.get(0)).usingRecursiveComparison().ignoringFields("icProject", "id", "password", "active", "userType", "projectUser", "role").isEqualTo(projectUsers.get(0).getUsers());
        assertThat(projectUserDtos.get(1)).usingRecursiveComparison().ignoringFields("icProject", "id", "password", "active", "userType", "projectUser", "role").isEqualTo(projectUsers.get(1).getUsers());
        assertThat(projectUserDtos.get(2)).usingRecursiveComparison().ignoringFields("icProject", "id", "password", "active", "userType", "projectUser", "role").isEqualTo(projectUsers.get(2).getUsers());
    }

    @DisplayName("Delete all project users of a project")
    @Test
    void givenValidProjectWhenDeleteProjectUsersThenDeletesProjectUsers() {
        List<ProjectUser> projectUsers = getProjectUsers();
        given(projectUserRepository.findAllByIcProjectId(anyLong())).willReturn(projectUsers);
        doNothing().when(projectUserRepository).delete(any(ProjectUser.class));
        projectUserService.deleteProjectUsers(1l);
        verify(projectUserRepository).findAllByIcProjectId(1l);
        verify(projectUserRepository, times(projectUsers.size())).delete(any(ProjectUser.class));
    }
}
