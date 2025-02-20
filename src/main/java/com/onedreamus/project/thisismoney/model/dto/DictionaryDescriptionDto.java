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

    private static DictionaryDescriptionDto create(String sentence, String description, String definition, Long dictionaryId, String term) {
        return DictionaryDescriptionDto.builder()
                .sentence(sentence)
                .description(description)
                .definition(definition)
                .term(term)
                .dictionaryId(dictionaryId)
                .build();
    }

    public static DictionaryDescriptionDto from(String sentence, Dictionary dictionary) {
        return create(sentence, dictionary.getDescription(),
                dictionary.getDefinition(), dictionary.getId(), dictionary.getTerm());
    }

    public static DictionaryDescriptionDto from(DictionarySentenceRequest request) {
        return create(request.getSentenceValue(), request.getDictionaryDescription(),
                request.getDictionaryDefinition(), request.getDictionaryId(),
                request.getDictionaryTerm());
    }

}
