package com.onedreamus.project.bank.model.dto;

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

    private boolean isUser;
    private String email;
    private String name;
    private String role;
    private Long socialId;
}