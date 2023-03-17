package com.school.science.fair.domain.exception;

import com.school.science.fair.domain.enumeration.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends DefaultException{

    public ResourceNotFoundException(HttpStatus status, ExceptionMessage reason) {
        super(status, reason);
    }

}
