package com.mk.exception;

import com.mk.model.ErrorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends Exception implements Serializable {

  private final String errorCode;
  private final String errorMessage;

  public ServiceException(ErrorInfo errorInfo) {
    this.errorCode = errorInfo.getCode();
    this.errorMessage = errorInfo.getMessage();
  }
}
