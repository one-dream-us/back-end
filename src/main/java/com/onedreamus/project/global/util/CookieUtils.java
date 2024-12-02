package com.onedreamus.project.global.util;

import jakarta.servlet.http.Cookie;
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

    public static String createDeleteCookie(){
        ResponseCookie cookie = ResponseCookie.from("Authorization", "")
                .maxAge(0)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .build();

        return cookie.toString();
    }
}
