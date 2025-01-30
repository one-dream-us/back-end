package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserCheckDto {

    private Integer userId;
    private String email;
    private String name;
    private String provider;
    private Long socialId;
    private String nickName;
    private String role;
    private Boolean isEmailDuplicated;

    public static UserCheckDto from(Users user, Boolean isEmailDuplicated) {
        return UserCheckDto.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .provider(user.getProvider())
            .socialId(user.getSocialId())
            .nickName(user.getNickname())
            .role(user.getRole())
            .isEmailDuplicated(isEmailDuplicated)
            .build();
    }
}
