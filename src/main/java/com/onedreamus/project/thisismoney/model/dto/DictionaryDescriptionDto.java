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

    public static DictionaryDescriptionDto defaultInstance(){
        return new DictionaryDescriptionDto();
    }

    /**
     * Dictionary 정보를 이용
     */
    public static DictionaryDescriptionDto from(String sentence, Dictionary dictionary) {
        return builderFrom(sentence, dictionary.getDescription(),
                dictionary.getDefinition(), dictionary.getId(), dictionary.getTerm());
    }

    /**
     * DictionarySentenceRequest 정보를 이용
     */
    public static DictionaryDescriptionDto from(DictionarySentenceRequest request) {
        return builderFrom(request.getSentenceValue(), request.getDictionaryDescription(),
                request.getDictionaryDefinition(), request.getDictionaryId(), request.getDictionaryTerm());
    }

    /**
     * Highlighted 정보를 포함한 DictionaryDescriptionDto 생성
     */
    public static DictionaryDescriptionDto fromWithHighlighting(DictionarySentenceRequest request, String highlightedDefinition, String highlightedSentence) {
        return builderFrom(highlightedSentence, request.getDictionaryDescription(),
                highlightedDefinition, request.getDictionaryId(), request.getDictionaryTerm());
    }

    /**
     * 중복을 제거 용
     */
    private static DictionaryDescriptionDto builderFrom(String sentence, String description, String definition, Long dictionaryId, String term) {
        return DictionaryDescriptionDto.builder()
                .sentence(sentence)
                .description(description)
                .definition(definition)
                .dictionaryId(dictionaryId)
                .term(term)
                .build();
    }

}
