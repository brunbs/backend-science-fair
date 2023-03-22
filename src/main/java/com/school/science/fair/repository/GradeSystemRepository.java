package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.GradeSystem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeSystemRepository extends JpaRepository<GradeSystem, Long> {

    List<GradeSystem> findAllByActiveTrue();

}
