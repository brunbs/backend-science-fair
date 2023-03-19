package com.school.science.fair.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    private Long registration;
    private String name;
    private String email;
    private String password;
    private boolean active;

}
