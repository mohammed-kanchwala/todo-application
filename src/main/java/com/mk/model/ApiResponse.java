package com.mk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class ApiResponse<T> {

    private HttpStatus status;
    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();
    private T messages;
    private T message;
    private ErrorInfo error;
}
