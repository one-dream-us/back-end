package com.onedreamus.project.global.config.jwt;

import com.onedreamus.project.bank.model.dto.CustomOAuth2User;
import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import com.onedreamus.project.bank.model.dto.UserDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.global.exception.CustomException;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.exception.FilterException;
import com.onedreamus.project.global.exception.LoginException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = null;
        Cookie[] cookies = request.getCookies();
        Cookie authorizationCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorizationCookie = cookie;
                authorization = cookie.getValue();
            }
        }

        if (authorization == null) {
            log.info("Token null");
            if (isScarpRequest(request.getServletPath())) {
                FilterException.throwException(response, ErrorCode.NEED_LOGIN);
                return;
            }

            FilterException.throwException(response, ErrorCode.TOKEN_NULL);
            return;
        }

        String token = authorization;

        // 토큰 만료 기간 확인
        if (jwtUtil.isExpired(token)) {
            log.info("token expired");

            // cookie 삭제
            authorizationCookie.setValue("");
            authorizationCookie.setPath("/");
            authorizationCookie.setMaxAge(0);
            response.addCookie(authorizationCookie);

            FilterException.throwException(response, ErrorCode.TOKEN_EXPIRED);
            return;
        }

        String name = jwtUtil.getUsername(token);
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        boolean isSocialLogin = jwtUtil.isSocialLogin(token);

        Authentication authentication = null;
        if (isSocialLogin) {

            CustomOAuth2User customOAuth2User =
                new CustomOAuth2User(UserDto.builder()
                    .name(name)
                    .role(role)
                    .email(email)
                    .build());

            authentication =
                new UsernamePasswordAuthenticationToken(
                    customOAuth2User, null, customOAuth2User.getAuthorities());
        } else {
            CustomUserDetails customUserDetails = new CustomUserDetails(Users.builder()
                .name(name)
                .email(email)
                .role(role)
                .build());

            authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
        }

        // 임시 session 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isScarpRequest(String path){
        if (path.contains("/scrap")){
            return true;
        }

        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path.equals("/user/logout")) {
            return true;
        } else if (path.startsWith("/login")) {
            return true;
        } else if (path.startsWith("/oauth2")) {
            return true;
        } else if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            return true;
        }else if (path.startsWith("/user/social/join") || path.startsWith("/user/social/unlink")){
            return true;
        }

        log.info("path : {}",path);

        return false;
    }
}
