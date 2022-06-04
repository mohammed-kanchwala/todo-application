package com.mk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class ApiResponse {

    private LocalDateTime responseTime = LocalDateTime.now();
    private Object message;
    private ErrorInfo error;
}
