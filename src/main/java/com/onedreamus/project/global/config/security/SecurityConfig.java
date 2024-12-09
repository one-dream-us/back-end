package com.onedreamus.project.global.config.security;

import com.onedreamus.project.thisismoney.repository.UserRepository;
import com.onedreamus.project.thisismoney.service.CustomOAuth2UserService;
import com.onedreamus.project.global.config.jwt.JWTFilter;
import com.onedreamus.project.global.config.jwt.JWTUtil;
import com.onedreamus.project.global.config.oauth2.CustomAuthorizationRequestResolver;
import com.onedreamus.project.global.config.oauth2.CustomSuccessHandler;
import com.onedreamus.project.global.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
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
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final UserRepository userRepository;
	private final CookieUtils cookieUtils;
	private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;


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
		http
			.cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

					CorsConfiguration corsConfiguration = new CorsConfiguration();
					corsConfiguration.setAllowedOrigins(
						List.of("http://localhost:3000", "https://thisismoney.site"));
					corsConfiguration.setAllowedMethods(
						List.of("GET", "POST", "DELETE", "PUT", "FETCH", "OPTIONS"));
					corsConfiguration.setAllowCredentials(true);
					corsConfiguration.setAllowedHeaders(List.of("*"));
					corsConfiguration.setMaxAge(360L);

					return corsConfiguration;
				}
			}));

		http
			.csrf(AbstractHttpConfigurer::disable) // csrf disable
			.formLogin(AbstractHttpConfigurer::disable) // form 로그인 disable
			.httpBasic(AbstractHttpConfigurer::disable); // http basic 인증 방식 disable

		// oauth2
		http
			.oauth2Login((oauth2) -> oauth2
				.authorizationEndpoint(authEndpoint -> authEndpoint
					.authorizationRequestResolver(customAuthorizationRequestResolver)
				)
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
					.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler) // login 성공 시
			);

//		http
//			.requestCache(cache -> cache
//				.requestCache(requestCache()));

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(
					"login/**", "/users/join", "/oauth2/**", "/swagger-ui.html",
					"/v3/api-docs/**", "/swagger-ui/**", "/api/v1/contents/**", "/api/v1/contents", "/api/v1/auth/check"
				).permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/api/v1/users/info", "/api/v1/scraps/**", "/api/v1/users/withdraw",
					"/api/v1/user/logout").hasAnyRole("USER")
				.anyRequest().authenticated());

		// 필터 추가
		http
//            .addFilterAt( // LoginFilter 등록
//                new LoginFilter(new ObjectMapper(), jwtUtil, authenticationManager(configuration)),
//                UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTFilter(jwtUtil, userRepository, cookieUtils),
				UsernamePasswordAuthenticationFilter.class); // JWTFilter 등록

		// session -> stateless
		http
			.sessionManagement(
				(session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
