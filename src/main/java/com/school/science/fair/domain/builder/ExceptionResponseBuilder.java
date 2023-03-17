package com.school.science.fair.domain.builder;

import com.school.science.fair.domain.enumeration.ExceptionMessage;
import com.school.science.fair.domain.response.ExceptionResponse;
import com.school.science.fair.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExceptionResponseBuilder {

  @Autowired
  private MessageUtil messageUtil;

  public ExceptionResponse getExceptionResponse(ExceptionMessage exceptionMessage) {
    return ExceptionResponse.builder()
            .timestamp(LocalDateTime.now())
            .message(messageUtil.getMessage(exceptionMessage))
            .build();
  }

}
