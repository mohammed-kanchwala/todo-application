package com.mk.exception;

import com.mk.constants.ErrorConstants;
import com.mk.model.ApiResponse;
import com.mk.model.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ToDoExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  public Object handleException(Exception exception) {
    log.error("Something went wrong: ", exception);
    return ApiResponse.builder().error(ErrorInfo.builder().code(ErrorConstants.SERVICE_EXCEPTION).message(ErrorConstants.SERVICE_EXCEPTION_MESSAGE).build()).build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  public Object handleAccessDeniedException(Exception exception) {
    log.error("Something went wrong: ", exception);
    return ApiResponse.builder().error(ErrorInfo.builder().code(ErrorConstants.ACCESS_DENIED).message(ErrorConstants.ACCESS_DENIED_MESSAGE).build()).build();
  }

  @ExceptionHandler({ServiceException.class, IllegalArgumentException.class, ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
  public Object handleCustomException(ServiceException ex) {
    log.error("ApiException thrown: ", ex);
    return ApiResponse.builder().error(ErrorInfo.builder().code(ex.getErrorCode()).message(ex.getErrorMessage()).build()).build();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Object handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<ErrorInfo> errors = new ArrayList<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.add(ErrorInfo.builder().code(fieldName).message(errorMessage).build());
    });
    return ApiResponse.builder().fieldErrors(errors).build();
  }
}
