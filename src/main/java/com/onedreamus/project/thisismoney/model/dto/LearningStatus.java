package com.onedreamus.project.thisismoney.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LearningStatus {

    private String username;
    private Integer totalScrap; // 총 스크랩 수
    private Integer totalGraduation; // 졸업한 단어 수
    private Integer totalBookmark; // 핵심노트 수
    private Integer accuracyRate; // 정답률

    public static LearningStatus from(String username, int totalGraduation, int totalBookmark, int accuracyRate, int totalWrongAnswer) {
        return LearningStatus.builder()
                .username(username)
                .totalScrap(totalGraduation + totalBookmark + totalWrongAnswer)
                .totalGraduation(totalGraduation)
                .accuracyRate(accuracyRate)
                .totalBookmark(totalBookmark)
                .build();
    }
}
