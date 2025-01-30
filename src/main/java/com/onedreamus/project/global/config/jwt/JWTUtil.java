package com.onedreamus.project.global.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;

    private final Long TEMP_JWT_EXPIRE_TIME = 3 * 60 * 1000L; // 3분

    public JWTUtil(@Value("${spring.jwt.secret-key}") String secret) {

        secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String getUsername(String token) {

        return getPayload(token)
            .get("username", String.class);
    }

    public String getEmail(String token) {

        return getPayload(token)
            .get("email", String.class);
    }

    public String getProvider(String token) {
        return getPayload(token)
            .get("provider", String.class);
    }

    public String getRole(String token) {

        return getPayload(token)
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
            return false;
        } catch (ExpiredJwtException e) {
            log.info("JWT 만료!!");
            return true;
        }
    }

    public Boolean isSocialLogin(String token) {
        return getPayload(token)
            .get("isSocialLogin", Boolean.class);
    }

    private Claims getPayload(String token) {

        try {
            return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String renewAccessToken(String refreshToken) {

        return createJwt(
            getUsername(refreshToken),
            getEmail(refreshToken),
            getRole(refreshToken),
            isSocialLogin(refreshToken),
            TokenType.ACCESS_TOKEN);
    }


    public Long getSocialId(String token) {
        return getPayload(token).get("socialId", Long.class);
    }

    public String createJwt(String username, String email, String role, boolean isSocialLogin,
        TokenType tokenType) {

        return Jwts.builder()
            .claim("username", username)
            .claim("email", email)
            .claim("role", role)
            .claim("isSocialLogin", isSocialLogin)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + tokenType.getExpiredTime()))
            .signWith(secretKey)
            .compact();
    }

    /**
     * MVP 이후 회원가입/로그인 프로세스 변경 시 적용
     */
//
    public String createTempJwt(String email, String provider) {

        return Jwts.builder()
            .claim("email", email)
            .claim("provider", provider)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + TEMP_JWT_EXPIRE_TIME))
            .signWith(secretKey)
            .compact();
    }
}
