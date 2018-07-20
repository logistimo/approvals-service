package com.logistimo.approval.exception;


import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Data;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */
@Data
public class BaseException extends RuntimeException {

  private final Object[] arguments;
  private String message;
  private int statusCode;
  private String errorCode;

  public BaseException(int statusCode, String errorCode, Object... arguments) {
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.arguments = arguments;
    this.message =
        String.format(ResourceBundle.getBundle("errors").getString(errorCode), arguments);
  }

  public BaseException(int statusCode, String errorCode) {
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.arguments = new Object[0];
    this.message = ResourceBundle.getBundle("errors").getString(errorCode);
  }

  public String getLocalizedMessage(Locale locale) {
    if (locale == null) {
      locale = Locale.ENGLISH;
    }
    return
        String.format(ResourceBundle.getBundle("errors", locale).getString(errorCode), arguments);
  }
}
