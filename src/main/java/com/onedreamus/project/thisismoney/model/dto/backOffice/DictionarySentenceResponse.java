package com.onedreamus.project.thisismoney.model.dto.backOffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DictionarySentenceResponse {

    private Long dictionaryId;
    private String dictionaryTerm;
    private String dictionaryDefinition;
    private String dictionaryDescription;
    private String sentenceValue;
    private Integer startIdx;
    private Integer endIdx;
}
