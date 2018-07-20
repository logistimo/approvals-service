package com.logistimo.approval.exception;

import java.util.Locale;

import lombok.Data;

/**
 * Created by charan on 13/07/17.
 */
@Data
public class ErrorResource {
  private String message;
  private String code;
  private int statusCode;

  public ErrorResource(BaseException exception, Locale locale) {
    this.code = exception.getErrorCode();
    this.message = exception.getLocalizedMessage(locale);
    this.statusCode = exception.getStatusCode();
  }
}
