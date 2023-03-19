package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.AreaOfKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaOfKnowledgeRepository extends JpaRepository<AreaOfKnowledge, Long> {

    Optional<AreaOfKnowledge> findByName(String name);

}
