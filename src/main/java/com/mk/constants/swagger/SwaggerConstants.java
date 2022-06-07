package com.mk.constants.swagger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerConstants {

    public static final String SWAGGER_DOC_API = "/v2/api-docs";
    public static final String SWAGGER_CONFIG_UI = "/configuration/ui";
    public static final String SWAGGER_RESOURCES = "/swagger-resources/**";
    public static final String SWAGGER_CONFIG = "/configuration/**";
    public static final String SWAGGER_HTML_UI = "/swagger-ui.html";
    public static final String WEB_JARS = "/webjars/**";

    public static final String SWAGGER_PACKAGE = "com.mk";
    public static final String SWAGGER_API_TITLE = "To Do Application APIs";
    public static final String API_DETAILS = "Api Details";
    public static final String SWAGGER_VERSION = "1.0";

}
