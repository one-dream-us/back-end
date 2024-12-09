package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryContentDto {

    private Long scrapId;
    private Long dictionaryId;
    private String term;
    private String details;
    private Long contentId;

}
