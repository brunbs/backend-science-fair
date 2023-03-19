package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmailOrRegistration(String email, Long registration);

}
