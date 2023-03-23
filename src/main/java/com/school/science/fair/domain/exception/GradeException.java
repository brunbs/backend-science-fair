package com.school.science.fair.domain.exception;

import com.school.science.fair.domain.enumeration.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GradeException extends DefaultException{

    public GradeException(HttpStatus status, ExceptionMessage reason) {
        super(status, reason);
    }

}
