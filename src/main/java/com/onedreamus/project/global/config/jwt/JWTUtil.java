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

    private final Long JWT_EXPIRE_TIME = 60 * 60 * 1000L; // 1시간

    public JWTUtil(@Value("${spring.jwt.secret-key}") String secret) {

        secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload()
            .get("username", String.class);
    }

    public String getEmail(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload()
            .get("email", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload()
            .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        try{
            Claims claims = getPayload(token);
            return false;
        }catch (ExpiredJwtException e){
            log.info("JWT 만료!!");
            return true;
        }
    }

    public Boolean isSocialLogin(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload()
            .get("isSocialLogin", Boolean.class);
    }

    private Claims getPayload(String token){
        return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload();
    }

    public String createJwt(String username, String email, String role, boolean isSocialLogin) {

        return Jwts.builder()
            .claim("username", username)
            .claim("email", email)
            .claim("role", role)
            .claim("isSocialLogin", isSocialLogin)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRE_TIME))
            .signWith(secretKey)
            .compact();
    }
}
