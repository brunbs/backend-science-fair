package com.school.science.fair.domain.exception;

import com.school.science.fair.domain.enumeration.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for exceptions associated with specific HTTP response status codes.
 */
@Getter
public class DefaultException extends RuntimeException {

  private static final long serialVersionUID = -7963947217892566012L;

  private final ExceptionMessage reason;
  private final HttpStatus status;

  /**
   * @param status the HTTP response status code
   * @param reason the exception message
   * @param cause  the error cause
   */
  public DefaultException(HttpStatus status, ExceptionMessage reason, Throwable cause) {
    super(reason.getMessageKey(), cause);
    this.status = status;
    this.reason = reason;
  }

  /**
   * @param status the HTTP response status code
   * @param reason the exception message
   */
  public DefaultException(HttpStatus status, ExceptionMessage reason) {
    super(reason.getMessageKey());
    this.status = status;
    this.reason = reason;
  }

  /**
   * @param reason the exception message
   */
  public DefaultException(ExceptionMessage reason) {
    super(reason.getMessageKey());
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.reason = reason;
  }

}
