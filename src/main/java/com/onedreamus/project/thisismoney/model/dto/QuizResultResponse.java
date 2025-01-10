package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuizResultResponse {

    private Integer graduationCnt; // 문제 내 졸업 노트 수
    private Integer totalWrong; // 문제 오답 수
    private Integer accuracyRate; // 문제 정답률
    private List<DictionaryStatusDto> resultDetails; // 용어 별 결과

    public static QuizResultResponse from(int graduationCnt, int totalWrong, int accuracyRate, List<DictionaryStatusDto> resultDetails) {
        return QuizResultResponse.builder()
                .graduationCnt(graduationCnt)
                .totalWrong(totalWrong)
                .accuracyRate(accuracyRate)
                .resultDetails(resultDetails)
                .build();
    }
}
