package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {


}