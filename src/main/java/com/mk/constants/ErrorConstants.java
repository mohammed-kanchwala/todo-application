package com.mk.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

  public static final String SERVICE_EXCEPTION = "SERVICE_EXCEPTION";
  public static final String SERVICE_EXCEPTION_MESSAGE = "The Service you are"
    + " trying is currently not available. Please try again later.";
    public static final String USER_DISABLED = "USER_DISABLED";
  public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";
}
