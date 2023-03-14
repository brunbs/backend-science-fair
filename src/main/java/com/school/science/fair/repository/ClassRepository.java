package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<Class, Long> {
}
