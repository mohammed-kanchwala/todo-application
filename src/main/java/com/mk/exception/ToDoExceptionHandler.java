package com.mk.exception;

import com.mk.constants.ErrorConstants;
import com.mk.model.ApiResponse;
import com.mk.model.ErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ToDoExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Object handleException(Exception exception) {
        log.error("Something went wrong: ", exception);
        return ApiResponse.builder()
                .error(ErrorInfo.builder()
                        .code(ErrorConstants.SERVICE_EXCEPTION)
                        .message(ErrorConstants.SERVICE_EXCEPTION_MESSAGE)
                        .build())
                .build();
    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Object handleAccessDeniedException(Exception exception) {
        log.error("Something went wrong: ", exception);
        return ApiResponse.builder()
                .error(ErrorInfo.builder()
                        .code(ErrorConstants.ACCESS_DENIED)
                        .message(ErrorConstants.ACCESS_DENIED_MESSAGE)
                        .build())
                .build();
    }

    @ExceptionHandler({ServiceException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public Object handleCustomException(ServiceException ex) {
        log.error("ApiException thrown: ", ex);
        return ApiResponse.builder()
                .error(ErrorInfo.builder()
                        .code(ex.getErrorCode())
                        .message(ex.getErrorMessage())
                        .build())
                .build();
    }

}
