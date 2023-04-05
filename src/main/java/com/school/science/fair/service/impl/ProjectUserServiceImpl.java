package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.ProjectUserDto;
import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.ProjectUser;
import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import com.school.science.fair.domain.mapper.IcProjectMapper;
import com.school.science.fair.domain.mapper.UserMapper;
import com.school.science.fair.repository.ProjectUserRepository;
import com.school.science.fair.service.ProjectUserService;
import com.school.science.fair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IcProjectMapper icProjectMapper;

    @Override
    public List<ProjectUserDto> insertUsersInProject(List<Long> usersRegistrations, ProjectDto projectDto) {

        IcProject project = icProjectMapper.DtoToEntity(projectDto);

        List<Users> users = new ArrayList<>();
        usersRegistrations.forEach(registration -> users.add(userMapper.dtoToEntity(userService.getUser(registration, UserTypeEnum.STUDENT))));

        List<ProjectUserDto> usersDto = new ArrayList<>();
        for(Users user : users) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setUsers(user);
            projectUser.setIcProject(project);
            projectUser.setRole(user.getUserType());
            projectUserRepository.save(projectUser);
            usersDto.add(ProjectUserDto.builder().name(user.getName()).role(user.getUserType()).email(user.getEmail()).registration(user.getRegistration()).build());
        }
        return usersDto;
    }

    @Override
    public List<ProjectUserDto> getProjectUsers(Long projectId) {
        List<ProjectUserDto> usersDto = new ArrayList<>();
        List<ProjectUser> projectUsers = projectUserRepository.findAllByIcProjectId(projectId);
        projectUsers.forEach(projectUser -> usersDto.add(buildProjectUserDto(projectUser)));
        return usersDto;
    }

    private ProjectUserDto buildProjectUserDto(ProjectUser projectUser) {
        return ProjectUserDto.builder()
                .name(projectUser.getUsers().getName())
                .role(projectUser.getUsers().getUserType())
                .email(projectUser.getUsers().getEmail())
                .registration(projectUser.getUsers().getRegistration())
                .build();
    }
}
