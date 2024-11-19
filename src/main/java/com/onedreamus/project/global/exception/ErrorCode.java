package com.onedreamus.project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // User
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."),
    NO_USER(HttpStatus.NOT_FOUND, "유저 정보가 존재하지 않습니다."),

    // Login
    WRONG_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 Content-Type 입니다. (required: application/json))"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
