package com.onedreamus.project.global.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenType {

    REFRESH_TOKEN("REFRESH-TOKEN", 60 * 24 * 5 * 60 * 1000L), // 5일
    ACCESS_TOKEN("ACCESS-TOKEN" ,10 * 60 * 1000L); // 10분

    private final String name;
    private final Long expiredTime;

}
