package com.onedreamus.project.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
            .csrf((auth) -> auth.disable()) // csrf disable
            .formLogin((auth) -> auth.disable()) // form 로그인 disable
            .httpBasic((auth) -> auth.disable()); // http basic 인증 방식 disable

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/join").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated());

        // session -> stateless
        http
            .sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
