package com.school.science.fair.repository;

import com.school.science.fair.domain.entity.Users;
import com.school.science.fair.domain.enumeration.UserTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailOrRegistration(String email, Long registration);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByRegistrationAndUserType(Long registration, UserTypeEnum userType);

}
