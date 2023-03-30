package com.school.science.fair.domain.entity;

import com.school.science.fair.domain.enumeration.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Users {

    @Id
    private Long registration;
    private String name;
    private String email;
    private String password;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private UserTypeEnum userType;
    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<ProjectUser> projectUser;

}
