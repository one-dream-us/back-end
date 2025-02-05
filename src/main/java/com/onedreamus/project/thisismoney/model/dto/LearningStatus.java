package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Users;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LearningStatus {

    private String username;
    private Integer totalScrap; // 총 스크랩 수
    private Integer totalGraduation; // 졸업한 단어 수
    private Integer totalKeyNote; // 핵심노트 수
    private Integer accuracyRate; // 정답률

    public static LearningStatus from(String username, int totalScrap, int totalGraduation, int totalKeyNote, int accuracyRate, int totalWrongAnswer) {
        return LearningStatus.builder()
                .username(username)
                .totalScrap(totalScrap + totalGraduation + totalKeyNote + totalWrongAnswer)
                .totalGraduation(totalGraduation)
                .accuracyRate(accuracyRate)
                .totalKeyNote(totalKeyNote)
                .build();
    }
}
