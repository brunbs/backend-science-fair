package com.school.science.fair.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    COORDINATOR("Coordinator"),
    TEACHER("Teacher"),
    STUDENT("Student");

    private String description;

    public static ExceptionMessage getNotFoundMessage(UserTypeEnum userTypeEnum) {
        if(userTypeEnum.equals(STUDENT)) {
            return ExceptionMessage.STUDENT_NOT_FOUND;
        }
        if(userTypeEnum.equals(TEACHER)) {
            return ExceptionMessage.TEACHER_NOT_FOUND;
        }
        if(userTypeEnum.equals(COORDINATOR)) {
            return ExceptionMessage.COORDINATOR_NOT_FOUND;
        }
        return null;
    }

}
