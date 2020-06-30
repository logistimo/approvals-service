package com.logistimo.approval.exception;


import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

  public static final String ERRORS_BUNDLE = "errors";
  private final transient Object[] arguments;
  private final String message;
  private final int statusCode;
  private final String errorCode;

  public BaseException(int statusCode, String errorCode, Object... arguments) {
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.arguments = arguments;
    this.message =
        String.format(ResourceBundle.getBundle(ERRORS_BUNDLE).getString(errorCode), arguments);
  }

  public BaseException(int statusCode, String errorCode) {
    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.arguments = new Object[0];
    this.message = ResourceBundle.getBundle(ERRORS_BUNDLE).getString(errorCode);
  }

  String getLocalizedMessage(Locale locale) {
    if (locale == null) {
      locale = Locale.ENGLISH;
    }
    return
        String.format(ResourceBundle.getBundle(ERRORS_BUNDLE, locale).getString(errorCode), arguments);
  }
}
