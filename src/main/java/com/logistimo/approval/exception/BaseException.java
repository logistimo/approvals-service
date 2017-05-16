package com.logistimo.approval.exception;


import lombok.Data;

/**
 * Created by nitisha.khandelwal on 12/05/17.
 */

@Data
public class BaseException extends RuntimeException {

  private String message;

  private int statusCode;

  public BaseException(int statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

}
