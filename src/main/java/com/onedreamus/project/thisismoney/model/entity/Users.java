package com.onedreamus.project.thisismoney.model.entity;

import com.onedreamus.project.thisismoney.model.dto.JoinDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String nickname;
    private String profileUrl;
    private String email;
    private String role;
    private String provider;
    private Long socialId;
    private boolean deleted;
    private String refreshToken;


    public static Users from(JoinDto dto) {
        return Users.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .role("ROLE_USER")
            .build();
    }

}
