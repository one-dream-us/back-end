package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.global.validator.ValidDictionarySentence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ValidDictionarySentence
public class DictionarySentenceRequest {

    private Long dictionaryId;
    private String dictionaryTerm;
    private String dictionaryDefinition;
    private String dictionaryDescription;

    @NotBlank(message = "sentenceValue는 필수 값입니다.")
    private String sentenceValue;
    @NotNull(message = "startIdx 는 필수 값입니다.")
    private Integer startIdx;
    @NotNull(message = "endIdx 는 필수 값입니다.")
    private Integer endIdx;
}
