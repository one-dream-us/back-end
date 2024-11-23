package com.onedreamus.project.bank.model.dto;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
