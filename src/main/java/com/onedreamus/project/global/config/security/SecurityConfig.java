package com.onedreamus.project.global.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onedreamus.project.global.config.jwt.JWTFilter;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration configuration;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors 설정
        //TODO: 추후 cors 허가 url 수정 필요 - 현재는 임시
        http
            .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setMaxAge(360L);

                    corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return corsConfiguration;
                }
            }));

        http
            .csrf(AbstractHttpConfigurer::disable) // csrf disable
            .formLogin(AbstractHttpConfigurer::disable) // form 로그인 disable
            .httpBasic(AbstractHttpConfigurer::disable); // http basic 인증 방식 disable

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/user/join").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/user/test").hasAnyRole("USER")
                .anyRequest().authenticated());

        // 필터 추가
        http
            .addFilterAt( // LoginFilter 등록
                new LoginFilter(new ObjectMapper(), jwtUtil, authenticationManager(configuration)),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class); // JWTFilter 등록

        // session -> stateless
        http
            .sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
