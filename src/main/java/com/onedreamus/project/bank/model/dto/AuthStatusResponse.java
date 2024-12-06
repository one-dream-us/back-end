package com.onedreamus.project.bank.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthStatusResponse {
	private boolean isLoggedIn;
}
