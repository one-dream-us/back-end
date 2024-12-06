package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.AuthStatusResponse;
import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthContoller {
	@GetMapping("/check")
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
