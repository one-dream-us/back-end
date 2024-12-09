package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.AuthStatusResponse;
import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth", description = "인증 관련 API")
public class AuthContoller {
	@GetMapping("/check")
	@Operation(summary = "사용자 로그인 조회", description = "사용자 로그인 상태를 조회합니다. 로그인 상태인 경우 true를 반환합니다.")
	public ResponseEntity<AuthStatusResponse> checkAuthStatus(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		boolean isLoggedIn = userDetails != null;

		return ResponseEntity.ok(
			AuthStatusResponse.builder()
				.isLoggedIn(isLoggedIn)
				.build()
		);
	}
}
