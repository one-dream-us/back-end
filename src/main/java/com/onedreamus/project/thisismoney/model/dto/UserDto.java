package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Users;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String name;
    private String nickname;
    private String email;
    private String role;
    private String provider;
    private Long socialId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto from(Users user){
        return UserDto.builder()
            .name(user.getName())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .role(user.getRole())
            .provider(user.getProvider())
                .socialId(user.getSocialId())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
