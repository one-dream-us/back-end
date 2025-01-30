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

	private final String[] PUBLIC_PATHS = {
			"login/**", "/users/join", "/oauth2/**", "/swagger-ui.html",
			"/v3/api-docs/**", "/swagger-ui/**",
			"/v1/contents/news", "/v1/contents/news/**",
			"/v1/contents", "/v1/auth/check",
			"/v1/users/join/social",
			"/v1/users/unlink/social", "/v1/contents/news/**", "/actuator/**",
	};

	private final String[] USER_PATHS = {
			"/v1/users/info", "/v1/scraps/**", "/v1/users/withdraw",
			"/v1/user/logout", "/v1/note/**","/v1/quiz/**", "/v1/users/quiz/first-attempt",
			"/v1/contents/news/{newsId}/users",
			"/v1/users/study-days/count"
	};

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
						List.of("http://localhost:3000", "https://thisismoney.site", "http://localhost:3001"));
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

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(PUBLIC_PATHS).permitAll()
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers(USER_PATHS).hasAnyRole("USER")
				.anyRequest().authenticated());

		// 필터 추가
		http
//            .addFilterAt( // LoginFilter 등록
//                new LoginFilter(new ObjectMapper(), jwtUtil, authenticationManager(configuration)),
//                UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JWTFilter(jwtUtil, userRepository, cookieUtils),
				UsernamePasswordAuthenticationFilter.class); // JWTFilter 등록

		// 에러 관리
		http
				.exceptionHandling(exceptions -> exceptions
						.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

		// session -> stateless
		http
			.sessionManagement(
				(session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
