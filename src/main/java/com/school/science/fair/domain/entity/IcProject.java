package com.school.science.fair.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "project")
@NoArgsConstructor
@AllArgsConstructor
public class IcProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private Topic topic;
    @ManyToOne
    private ScienceFair scienceFair;
    
}
