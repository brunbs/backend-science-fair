package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.IcProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IcProjectRepository extends JpaRepository<IcProject, Long> {


}
