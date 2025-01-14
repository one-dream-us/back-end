package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DictionaryDescriptionDto {

    private String sentence;
    private String description;
    private String definition;
    private Long dictionaryId;
    private String term;

    public static DictionaryDescriptionDto from(String sentence, Dictionary dictionary) {
        return DictionaryDescriptionDto.builder()
                .sentence(sentence)
                .definition(dictionary.getDefinition())
                .description(dictionary.getDescription())
                .dictionaryId(dictionary.getId())
                .term(dictionary.getTerm())
                .build();
    }

}
