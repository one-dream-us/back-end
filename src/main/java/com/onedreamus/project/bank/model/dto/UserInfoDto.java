package com.onedreamus.project.bank.model.dto;

import com.onedreamus.project.bank.model.entity.Users;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {


    private String name;
    private String nickname;
    private String email;
    private String provider;
    private LocalDateTime createdAt;

    public static UserInfoDto from(Users user) {
        return UserInfoDto.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .provider(user.getProvider())
                .build();
    }
}