package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.IcProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IcProjectRepository extends JpaRepository<IcProject, Long> {

    List<IcProject> findAllByScienceFairId(Long scienceFairId);
}
