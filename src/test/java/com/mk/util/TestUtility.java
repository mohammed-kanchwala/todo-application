package com.mk.util;

import com.mk.constants.UrlConstants;
import lombok.Data;

public class TestUtility {

    public static String token;
    public static String createUserURL(int port, String urlEndPoint) {
        return UrlConstants.HTTP_LOCALHOST + port + UrlConstants.API_URL + UrlConstants.USER_URL + urlEndPoint;
    }

    public static String createToDoURL(int port, String urlEndPoint) {
        return UrlConstants.HTTP_LOCALHOST + port + UrlConstants.API_URL + UrlConstants.TODO_URL + urlEndPoint;
    }

}
