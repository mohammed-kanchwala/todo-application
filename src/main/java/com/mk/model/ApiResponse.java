package com.mk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	private HttpStatus status;
	@Builder.Default
	private LocalDateTime responseTime = LocalDateTime.now();
	private T messages;
	private T message;
	private ErrorInfo error;
	private List<ErrorInfo> fieldErrors;
}
