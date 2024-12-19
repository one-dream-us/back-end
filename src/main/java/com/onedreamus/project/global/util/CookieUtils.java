package com.onedreamus.project.global.util;

import com.onedreamus.project.global.config.jwt.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CookieUtils {

    @Value("${spring.cookie.domain.server}")
    private String SERVER_DOMAIN;

    @Value("${spring.cookie.domain.local}")
    private String LOCAL_DOMAIN;

    public String create(String name, String value, String domain) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
            .maxAge(60 * 60 * 24 * 5)
            .domain(domain)
            .secure(true)
            .path("/")
            .httpOnly(true)
            .sameSite("None")
            .build();

        return cookie.toString();
    }

    public String createDeleteCookie(String name, String domain) {
        ResponseCookie cookie = ResponseCookie.from(name)
            .maxAge(0)
            .domain(domain)
            .secure(true)
            .path("/")
            .httpOnly(true)
            .sameSite("None")
            .build();

        return cookie.toString();
    }

    public List<String> createAllCookies(String name, String value) {
        String localCookie = create(name, value, LOCAL_DOMAIN);
        String serverCookie = create(name, value, SERVER_DOMAIN);

        return new ArrayList<>(List.of(localCookie, serverCookie));
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        response.addHeader(HttpHeaders.SET_COOKIE, createDeleteCookie(name, SERVER_DOMAIN));
        response.addHeader(HttpHeaders.SET_COOKIE, createDeleteCookie(name, LOCAL_DOMAIN));
    }

    public void deleteAllCookie(HttpServletResponse response, List<String> names) {
        for (String name : names) {
            deleteCookie(response, name);
        }
    }
}
