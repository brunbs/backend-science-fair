package com.school.science.fair.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    COORDINATOR,
    TEACHER,
    STUDENT;

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
