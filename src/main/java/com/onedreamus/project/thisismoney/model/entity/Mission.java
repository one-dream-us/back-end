package com.onedreamus.project.thisismoney.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    private Boolean newsLearnStatus;
    private Boolean quizSolveStatus;

    private LocalDate date;
    private int continuousDays;

    public static Mission from(Users user, int continuousDays){
        return Mission.builder()
                .user(user)
                .newsLearnStatus(false)
                .quizSolveStatus(false)
                .date(LocalDate.now())
                .continuousDays(continuousDays)
                .build();
    }
}
