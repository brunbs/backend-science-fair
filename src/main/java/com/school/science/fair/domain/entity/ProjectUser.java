package com.school.science.fair.domain.entity;

import com.school.science.fair.domain.enumeration.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private IcProject icProject;

    @ManyToOne
    private Users users;

    private UserTypeEnum role;

}
