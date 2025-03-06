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
    NO_APPROPRIATE_STATUS(HttpStatus.BAD_REQUEST, "적절한 상태값이 없습니다."),

    // Scarp
    HISTORY_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 히스토리 입니다."),
    ALREADY_SCRAPPED(HttpStatus.BAD_REQUEST, "이미 스크랩된 용어이거나, 학습중인 용어입니다."),

    // Bookmark
    BOOKMARK_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 핵심 노트에 존재하는 용어 입니다."),
    BOOKMARK_NOT_EXIST(HttpStatus.NOT_FOUND, "핵심노트에 존재하지 않는 용어 입니다."),
    GRADUATED_ALREADY(HttpStatus.BAD_REQUEST, "이미 졸업한 단어이거나, 오답노트에 있는 단어입니다."),

    // GraduateNote

    // WrongAnswerNote
    WRONG_ANSWER_NOTE_NOT_EXIST(HttpStatus.NOT_FOUND, "오답노트에 존재하지 않는 용어입니다."),

    // Quiz
    NOT_ENOUGH_DICTIONARY(HttpStatus.BAD_REQUEST, "핵심단어와 오답노트에 단어가 총 3개 이상 필요합니다."),

    // S3
    IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패했습니다."),
    AWS_SDK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3 SDK 에러가 발생하여 정보를 처리할 수 없습니다."),
    INVALID_URL(HttpStatus.BAD_REQUEST, "잘못된 URL 정보 입니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "파일 확장자가 없습니다."),

    // Mission
    MISSING_DATE_OR_MONTH_PARAM(HttpStatus.BAD_REQUEST, "'date' 또는 'month' 값이 필요합니다."),
    INVALID_MONTH_FORMAT(HttpStatus.BAD_REQUEST, "'month' 형식이 잘못되었습니다."),

    // Back office
    DATE_DUPLICATION(HttpStatus.BAD_REQUEST, "동일한 날짜에 이미 예약이 있습니다."),
    ONLY_ONE_DATA_ACCEPTABLE(HttpStatus.BAD_REQUEST, "썸네일 URL, 썸네일 이미지 중 하나의 값만 필요합니다."),
    FAIL_THUMBNAIL_URL(HttpStatus.BAD_REQUEST, "썸네일 URL 생성에 실패했습니다. 요청 데이터를 확인 해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
