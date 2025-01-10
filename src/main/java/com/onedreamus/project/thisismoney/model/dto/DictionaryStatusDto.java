package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DictionaryStatusDto {

    private DictionaryStatus status;
    private String term;
    private Integer correctCnt;
    private Integer wrongCnt;
    private Boolean isCorrect;

    public static DictionaryStatusDto from(QuizResult quizResult, String term, int correctCnt, int wrongCnt) {
        return DictionaryStatusDto.builder()
                .status(quizResult.getStatus())
                .term(term)
                .correctCnt(correctCnt)
                .wrongCnt(wrongCnt)
                .isCorrect(quizResult.isCorrect())
                .build();
    }

}
