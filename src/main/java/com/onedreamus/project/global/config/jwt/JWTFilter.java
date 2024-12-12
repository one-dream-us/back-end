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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    @Value("${spring.cookie.domain.server}")
    private String SERVER_DOMAIN;

    @Value("${spring.cookie.domain.local}")
    private String LOCAL_DOMAIN;

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();

        if (path.equals("/v1/auth/check")) {  // 추가된 부분
            if (cookies != null) {
                setAuthenticationIfValidToken(cookies, response);
            }
            filterChain.doFilter(request, response);
            return;
        }

        if (cookies == null) {
            // 스크랩 요청인 경우만 로그인 필요
            if (isScrapRequest(request.getServletPath())) {
                FilterException.throwException(response, ErrorCode.NEED_LOGIN);
                return;
            }
            // 스크랩 요청이 아닌 경우 필터 통과
            filterChain.doFilter(request, response);
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
            if (isScrapRequest(request.getServletPath())) {
                FilterException.throwException(response, ErrorCode.NEED_LOGIN);
                return;
            }
            FilterException.throwException(response, ErrorCode.TOKEN_NULL);
            return;
        }

        boolean isAccessTokenExpired = jwtUtil.isExpired(accessToken);

        String email = jwtUtil.getEmail(accessToken);
        Optional<Users> optionalUser = userRepository.findByEmail(email);
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
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), newAccessToken, SERVER_DOMAIN));
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), newAccessToken, LOCAL_DOMAIN));
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(optionalUser.get());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
            null, customUserDetails.getAuthorities());

        // session 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private boolean isScrapRequest(String path) {
        return path.contains("/scraps");
    }

    private void setAuthenticationIfValidToken(Cookie[] cookies, HttpServletResponse response)
        throws IOException {
        String accessToken = null;
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TokenType.ACCESS_TOKEN.getName())) {
                accessToken = cookie.getValue();
                continue;
            }

            if (cookie.getName().equals(TokenType.REFRESH_TOKEN.getName())) {
                refreshToken = cookie.getValue();
            }
        }

        if (accessToken == null) {
            return;
        }

        boolean isAccessTokenExpired = jwtUtil.isExpired(accessToken);

        String email = jwtUtil.getEmail(accessToken);
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return;
        }

        if (isAccessTokenExpired) { // access-token 만료
            Users user = optionalUser.get();
            if (user.isDeleted()) {
                return;
            }

            // DB refresh-token 과 유저가 준 refresh-token 이 동일한지 확인
            if (!user.getRefreshToken().equals(refreshToken)) {
                return;
            }

            // refresh-token 만료 기간 검사
            if (jwtUtil.isExpired(refreshToken) || user.getRefreshToken().isEmpty()) {
                return;
            }

            String newAccessToken = jwtUtil.renewAccessToken(refreshToken);
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), newAccessToken, SERVER_DOMAIN));
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtils.create(TokenType.ACCESS_TOKEN.getName(), newAccessToken, LOCAL_DOMAIN));
        }

        CustomUserDetails userDetails = new CustomUserDetails(optionalUser.get());
        Authentication auth = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getServletPath();
        List<String> publicPaths = List.of(
            "/login/**",
            "/users/join",
            "/oauth2/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v1/contents/**",
            "/v1/contents",
                "/v1/users/join/social", "/v1/users/unlink/social"
        );

        return publicPaths.stream().anyMatch(publicPath ->
            path.startsWith(publicPath.replace("/**", "")) ||
                path.matches(publicPath.replace("**", ".*"))
        );
    }

}
