package com.onedreamus.project.global.config.jwt;

import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.exception.FilterException;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        // 쿠키가 없으면 다음 필터로 넘어감.
        if (cookies == null) {
            filterChain.doFilter(request,response);
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TokenType.ACCESS_TOKEN.getName())) {
                accessToken = cookie.getValue();
            } else if (cookie.getName().equals(TokenType.REFRESH_TOKEN.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isAccessTokenExpired = jwtUtil.isExpired(accessToken);

        String email = jwtUtil.getEmail(accessToken);
        Optional<Users> optionalUser = userRepository.findByEmailAndDeleted(email, false);
        if (optionalUser.isEmpty()) {
            FilterException.throwException(response, ErrorCode.NO_USER);
            return;
        }

        // 1. access-token 확인
        if (isAccessTokenExpired) {
            // access-token 이 만료된 경우
            // 2. refresh-token 확인
            Users user = optionalUser.get();
            if (user.isDeleted()) {
                FilterException.throwException(response, ErrorCode.NO_USER);
                return;
            }

            // DB refresh-token 과 유저가 준 refresh-token 이 동일한지 확인
            if (!user.getRefreshToken().equals(refreshToken)) {
                FilterException.throwException(response, ErrorCode.REFRESH_TOKEN_DIFFERENT);
                return;
            }

            // refresh-token 만료 기간 검사
            if (jwtUtil.isExpired(refreshToken) || user.getRefreshToken().isEmpty()) {
                // 만료된 경우 -> 재로그인 필요
                log.info("[Refresh token is expired-재로그인 필요] 이메일: {}", email);

                List<String> allTokenType = TokenType.getAllTokenName();
                cookieUtils.deleteAllCookie(response, allTokenType);

                FilterException.throwException(response, ErrorCode.TOKEN_EXPIRED);
                return;
            }

            // 만료 X -> access-token 재발급
            String newAccessToken = jwtUtil.renewAccessToken(refreshToken);
            cookieUtils.createAllCookies(TokenType.ACCESS_TOKEN.getName(), newAccessToken)
                    .forEach(cookie -> response.addHeader(HttpHeaders.SET_COOKIE, cookie));
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(optionalUser.get());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
            null, customUserDetails.getAuthorities());

        // session 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
