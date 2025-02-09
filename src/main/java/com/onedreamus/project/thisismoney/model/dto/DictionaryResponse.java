package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DictionaryResponse {

    private Long id;
    private String term;
    private String definition;
    private String description;

    public static DictionaryResponse from(Dictionary dictionary) {
        return DictionaryResponse.builder()
            .id(dictionary.getId())
            .term(dictionary.getTerm())
            .definition(dictionary.getDefinition())
            .description(dictionary.getDescription())
            .build();

    }

}
