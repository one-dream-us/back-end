package com.onedreamus.project.thisismoney.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onedreamus.project.thisismoney.model.constant.DictionaryStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizResult {

    private Long dictionaryId;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
    private DictionaryStatus status;

}
