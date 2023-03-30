package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.IcProject;
import com.school.science.fair.domain.entity.ProjectGrade;
import com.school.science.fair.domain.entity.ProjectGradePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectGradeRepository extends JpaRepository<ProjectGrade, ProjectGradePK> {

    List<ProjectGrade> findByIdIcProjectId(Long id);

}
