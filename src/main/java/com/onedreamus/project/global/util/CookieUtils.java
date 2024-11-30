package com.onedreamus.project.global.util;

import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static String create(String value){

        ResponseCookie cookie = ResponseCookie.from("Authorization", value)
            .maxAge(60 * 60 * 60)
            .secure(true)
            .path("/")
            .httpOnly(true)
            .sameSite("None")
            .build();

        return cookie.toString();
    }
}
