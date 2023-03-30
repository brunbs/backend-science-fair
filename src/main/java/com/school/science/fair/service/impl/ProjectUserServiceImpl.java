package com.school.science.fair.service.impl;

import com.school.science.fair.domain.dto.ProjectDto;
import com.school.science.fair.domain.dto.UserProjectDto;
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
    public List<UserProjectDto> insertUsersInProject(List<Long> usersRegistrations, ProjectDto projectDto) {

        IcProject project = icProjectMapper.DtoToEntity(projectDto);

        List<Users> users = new ArrayList<>();
        usersRegistrations.forEach(registration -> users.add(userMapper.dtoToEntity(userService.getUser(registration, UserTypeEnum.STUDENT))));

        List<UserProjectDto> usersDto = new ArrayList<>();
        for(Users user : users) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setUsers(user);
            projectUser.setIcProject(project);
            projectUser.setRole(user.getUserType());
            projectUserRepository.save(projectUser);
            usersDto.add(UserProjectDto.builder().name(user.getName()).email(user.getEmail()).registration(user.getRegistration()).build());
        }
        return usersDto;
    }
}
