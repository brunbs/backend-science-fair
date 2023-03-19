package com.school.science.fair.domain.enumeration;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

  // Generic exception messages
  INTERNAL_SERVER_ERROR("error.generic.internal-server-error"),
  EMAIL_ALREADY_EXISTS("error.email.already-exists-error"),

  // Class exception messages
  CLASS_NOT_FOUND("error.class.not-found-error"),

  // Student exception messages
  STUDENT_NOT_FOUND("error.student.not-found-error"),
  STUDENT_ALREADY_EXISTS("error.student.already-exists-error");

  private final String messageKey;

  ExceptionMessage(String messageKey) {
    this.messageKey = messageKey;
  }

  @Override
  public String toString() {
    return messageKey;
  }

}
