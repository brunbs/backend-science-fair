package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {


}
