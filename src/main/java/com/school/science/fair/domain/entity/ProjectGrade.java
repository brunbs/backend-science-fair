package com.school.science.fair.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "project_grade")
public class ProjectGrade {

    @JsonIgnore
    @EmbeddedId
    private ProjectGradePK id = new ProjectGradePK();
    private Double gradeValue;

    public ProjectGrade() {}

    public ProjectGrade(IcProject icProject, Grade grade) {
        super();
        id.setIcProject(icProject);
        id.setGrade(grade);
        this.gradeValue = 0.0;
    }

    public String getGradeName() {
        return id.getGrade().getName();
    }

    public Double getMaxValue() {
        return id.getGrade().getMaxValue();
    }

    public String getGradeDescription() {
        return id.getGrade().getDescription();
    }

    public Long getGradeId() {
        return id.getGrade().getId();
    }


}
