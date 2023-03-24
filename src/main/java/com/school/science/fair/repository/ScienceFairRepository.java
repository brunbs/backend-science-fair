package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Class;
import com.school.science.fair.domain.entity.ScienceFair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceFairRepository extends JpaRepository<ScienceFair, Long> {



}
