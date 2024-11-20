package com.onedreamus.project.global.config.jwt;

import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.exception.FilterException;
import com.onedreamus.project.global.exception.LoginException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        String authorization = request.getHeader("Authorization");
        log.info("<<< JWTFilter >>>");
        // Header에 토큰이 없는 경우
        if (authorization == null || !authorization.startsWith("Bearer")){
            log.info("token null");
            filterChain.doFilter(request, response);
            FilterException.throwException(response, ErrorCode.TOKEN_NULL);
            return;
        }

        String token = authorization.split(" ")[1];

        // 토큰 만료 기간 확인
        if (jwtUtil.isExpired(token)) {
            log.info("token expired");
            filterChain.doFilter(request, response);
            FilterException.throwException(response, ErrorCode.TOKEN_EXPIRED);
            return;
        }

        String name = jwtUtil.getUsername(token);
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        CustomUserDetails customUserDetails = new CustomUserDetails(Users.builder()
            .name(name)
            .password("temppassword")
            .email(email)
            .role(role)
            .build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
            null, customUserDetails.getAuthorities());

        // 임시 session 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("token filter success, name: {}, email: {}, role: {}", name, email, role);
        filterChain.doFilter(request, response);
    }
}
