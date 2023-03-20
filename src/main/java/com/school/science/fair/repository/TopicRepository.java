package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {


}
