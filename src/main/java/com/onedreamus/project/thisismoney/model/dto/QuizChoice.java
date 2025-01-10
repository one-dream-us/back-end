package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuizChoice {

    private Long dictionaryId;
    private String term;
    private String detail;
    private Integer quizNum;
    private DictionaryStatus status; // 용어 위치

    public static QuizChoice from(DictionaryQuiz dictionaryQuiz, int quizNum) {
        return QuizChoice.builder()
                .dictionaryId(dictionaryQuiz.getDictionaryId())
                .term(dictionaryQuiz.getTerm())
                .detail(dictionaryQuiz.getDetails())
                .quizNum(quizNum)
                .status(dictionaryQuiz.getStatus())
                .build();
    }

}
