package com.onedreamus.project.thisismoney.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MissionStatusDto {

    private Boolean news;
    private Boolean quiz;

    public static MissionStatusDto from(boolean newsLearnStatus, boolean quizSolveStatus){
        return MissionStatusDto.builder()
                .news(newsLearnStatus)
                .quiz(quizSolveStatus)
                .build();
    }
}
