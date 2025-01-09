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
    UNLINK_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "소셜로그인 연결 해제 실패"),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "유저 정보가 동일하지 않습니다."),

    // Login
    WRONG_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 Content-Type 입니다. (required: application/json))"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
    TOKEN_NULL(HttpStatus.BAD_REQUEST, "토큰을 찾을 수 없습니다. 로그인 해주세요."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료 되었습니다. 다시 로그인 해주세요."),
    REFRESH_TOKEN_DIFFERENT(HttpStatus.BAD_REQUEST, "Refresh-Token이 옳바르지 않습니다."),


    // Scrap
    NEED_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 요청 입니다. 로그인 해주세요."),

    // Content
    CONTENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 컨텐츠 입니다."),

    // Dictionary
    DICTIONARY_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 용어 입니다."),

    // Scarp
    SCRAP_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 스크랩 입니다."),
    ALREADY_SCRAPPED(HttpStatus.BAD_REQUEST, "이미 스크랩된 항목 입니다."),

    // KeyNote
    KEYNOTE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 핵심 노트에 존재하는 용어 입니다."),
    KEYNOTE_NOT_EXIST(HttpStatus.NOT_FOUND, "핵심노트에 존재하지 않는 용어 입니다."),
    GRADUATED_ALREADY(HttpStatus.BAD_REQUEST, "이미 졸업한 용어 입니다.");

    // GraduateNote


    private final HttpStatus httpStatus;
    private final String message;
}
