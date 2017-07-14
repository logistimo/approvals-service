package com.logistimo.approval.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@ControllerAdvice
public class BaseExceptionMapper extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResource> handleException(BaseException exception, Locale locale)
      throws IOException {

    return new ResponseEntity<>(new ErrorResource(exception, locale),
        HttpStatus.valueOf(exception.getStatusCode()));

  }

}
