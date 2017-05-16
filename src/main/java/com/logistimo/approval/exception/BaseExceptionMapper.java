package com.logistimo.approval.exception;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@ControllerAdvice
public class BaseExceptionMapper extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler(BaseException.class)
  public void handleException(HttpServletResponse response, BaseException exception)
      throws IOException {
    response.sendError(exception.getStatusCode(), exception.getMessage());
  }

}
