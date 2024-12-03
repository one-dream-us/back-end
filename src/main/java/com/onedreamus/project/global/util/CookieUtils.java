package com.onedreamus.project.global.util;

import com.onedreamus.project.global.config.jwt.TokenType;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static String create(String name, String value){

        ResponseCookie cookie = ResponseCookie.from(name, value)
            .maxAge(60 * 60 * 60)
            .secure(true)
            .path("/")
            .httpOnly(true)
            .sameSite("None")
            .build();

        return cookie.toString();
    }

    public static String createDeleteCookie(String name){
        ResponseCookie cookie = ResponseCookie.from(name)
                .maxAge(0)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .build();

        return cookie.toString();
    }
}
