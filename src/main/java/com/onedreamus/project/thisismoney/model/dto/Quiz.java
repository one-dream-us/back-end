package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Quiz {

    private String question;
    private Integer answerNum;
    private QuizChoice[] choices;

    public static Quiz from(int answerNum, QuizChoice[] choices) {
        return Quiz.builder()
                .question(choices[answerNum].getDetail())
                .answerNum(answerNum + 1)
                .choices(choices)
                .build();
    }

}
