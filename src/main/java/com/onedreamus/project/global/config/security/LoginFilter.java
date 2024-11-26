package com.onedreamus.project.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import com.onedreamus.project.bank.model.dto.LoginDto;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.exception.FilterException;
import com.onedreamus.project.global.exception.LoginException;
import com.onedreamus.project.global.exception.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.lang.model.type.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {


    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
        DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    private final ObjectMapper objectMapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    public LoginFilter(ObjectMapper objectMapper, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER, authenticationManager); // "/login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 로그인 인증
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws IOException {

        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            FilterException.throwException(response, ErrorCode.WRONG_CONTENT_TYPE);
        }

        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 로그인 성공 시
     * - JWT 발급
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain, Authentication authResult) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, email, role, false);
        response.addHeader("Authorization", "Bearer " + token);

        log.info("Login Success - username: {} / email: {}", username, email);
    }

    /**
     * 로그인 실패 시
     */
    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed) throws IOException {

        // 로그인 실패 에러 반환
        FilterException.throwException(response, ErrorCode.LOGIN_FAIL);
    }
}
