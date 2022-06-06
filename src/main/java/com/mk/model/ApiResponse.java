package com.mk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @Builder.Default
    private LocalDateTime responseTime = LocalDateTime.now();
    private T message;
    private ErrorInfo error;
}
