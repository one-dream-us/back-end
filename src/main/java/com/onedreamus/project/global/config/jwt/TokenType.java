package com.onedreamus.project.global.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TokenType {

    REFRESH_TOKEN("REFRESH-TOKEN", 60 * 24 * 5 * 60 * 1000L), // 5일
    ACCESS_TOKEN("ACCESS-TOKEN" ,10 * 60 * 1000L); // 10분

    private final String name;
    private final Long expiredTime;

    public static List<String> getAllTokenName(){
        return Arrays.stream(TokenType.values())
                .map(TokenType::getName)
                .toList();
    }

}