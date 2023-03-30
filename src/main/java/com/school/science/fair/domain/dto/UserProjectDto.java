package com.school.science.fair.domain.dto;

import com.school.science.fair.domain.enumeration.UserTypeEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProjectDto {
    private Long registration;
    private String name;
    private String email;
}
