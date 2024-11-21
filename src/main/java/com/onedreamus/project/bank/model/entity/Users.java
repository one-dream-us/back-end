package com.onedreamus.project.bank.model.entity;

import com.onedreamus.project.bank.model.dto.JoinDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users extends BaseEntity{

    //TODO: ERD 설계 및 필드 값 수정 필요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private String role;

    public void encodePassword(BCryptPasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public static Users from(JoinDto dto) {
        return Users.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .role("ROLE_USER")
            .build();
    }
}
