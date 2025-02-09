package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DictionarySentenceRequest {

    private Long dictionaryId;
    private String dictionaryTerm;
    private String dictionaryDefinition;
    private String dictionaryDescription;
    private String sentenceValue;
    private Integer startIdx;
    private Integer endIdx;
}
