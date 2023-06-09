package com.school.science.fair.domain.enumeration;

import lombok.Getter;

@Getter
public enum ExceptionMessage {

  // Generic exception messages
  INTERNAL_SERVER_ERROR("error.generic.internal-server-error"),
  EMAIL_ALREADY_EXISTS("error.email.already-exists-error"),

  // Class exception messages
  CLASS_NOT_FOUND("error.class.not-found-error"),

  //Area of Knowledge and Topics messages
  AREA_OF_KNOWLEDGE_ALREADY_EXISTS("error.area-of-knowledge.already-exists-error"),
  AREA_OF_KNOWLEDGE_NOT_FOUND("error.area-of-knowledge.not-found-error"),

  // Grade System exception messages
  GRADE_VALUES_BIGGER_THEN_GRADE_SYSTEM_MAX_VALUE("error.grade.grades-values-bigger-than-grade-system-max-value-error"),
  GRADE_SYSTEM_NOT_FOUND("error.grade-system.not-found-error"),

  // Science Fair Exception messages
  SCIENCE_FAIR_NOT_FOUND("error.science-fair.not-found.error"),

  // Student exception messages
  STUDENT_NOT_FOUND("error.student.not-found-error"),
  USER_NOT_FOUND("error.user.not-found-error"),
  TEACHER_NOT_FOUND("error.teacher.not-found-error"),
  COORDINATOR_NOT_FOUND("error.coordinator.not-found-error"),

  USER_ALREADY_EXISTS("error.user.already-exists-error");

  private final String messageKey;

  ExceptionMessage(String messageKey) {
    this.messageKey = messageKey;
  }

  @Override
  public String toString() {
    return messageKey;
  }

}
