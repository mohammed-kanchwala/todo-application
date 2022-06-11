package com.mk.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

	public static final String SERVICE_EXCEPTION = "SERVICE_EXCEPTION";
	public static final String SERVICE_EXCEPTION_MESSAGE = "The Service you are"
																												 +
																												 " trying is currently not available. Please try again later.";
	public static final String USER_DISABLED = "USER_DISABLED";
	public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
	public static final String ACCESS_DENIED = "ACCESS_DENIED";
	public static final String ACCESS_DENIED_MESSAGE =
					"You do not have access to this API or Request is Invalid. Please check and try again.";
	public static final String LINE_NAME_VALIDATION_MESSAGE =
					"Line Name can not be more than 100 characters";
	public static final String TASK_TITLE_VALIDATION_MESSAGE =
					"Line Name can not be more than 100 characters";
}
