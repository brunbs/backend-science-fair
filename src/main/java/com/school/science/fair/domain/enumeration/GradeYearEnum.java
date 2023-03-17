package com.school.science.fair.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GradeYearEnum {
    EF_5ANO("5 Ano - Ensino Fundamental"),
    EF_6ANO("6 Ano - Ensino Fundamental"),
    EF_7ANO("7 Ano - Ensino Fundamental"),
    EF_8ANO("8 Ano - Ensino Fundamental"),
    EF_9ANO("9 Ano - Ensino Fundamental"),
    EM_1ANO("1 Ano - Ensino Médio"),
    EM_2ANO("2 Ano - Ensino Médio"),
    EM_3ANO("3 Ano - Ensino Médio");

    private String description;

}
