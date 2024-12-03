package com.onedreamus.project.global.util;

import com.onedreamus.project.global.config.jwt.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CookieUtils {

    public String create(String name, String value){

        ResponseCookie cookie = ResponseCookie.from(name, value)
            .maxAge(60 * 60 * 60)
            .secure(true)
            .path("/")
            .httpOnly(true)
            .sameSite("None")
            .build();

        return cookie.toString();
    }

    public String createDeleteCookie(String name){
        ResponseCookie cookie = ResponseCookie.from(name)
                .maxAge(0)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .build();

        return cookie.toString();
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        response.addHeader(HttpHeaders.SET_COOKIE, createDeleteCookie(name));
    }

    public void deleteAllCookie(HttpServletResponse response, List<String> names){
        for (String name : names) {
            deleteCookie(response, name);
        }
    }
}
