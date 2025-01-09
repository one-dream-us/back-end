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
public class DictionaryQuiz {

    private Long dictionaryId;
    private String term;
    private String details;
    private DictionaryStatus status;

    public static DictionaryQuiz from(Dictionary dictionary, DictionaryStatus status) {
        return DictionaryQuiz.builder()
                .dictionaryId(dictionary.getId())
                .term(dictionary.getTerm())
                .details(dictionary.getDetails())
                .build();
    }
}
