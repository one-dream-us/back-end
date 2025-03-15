package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DictionaryNewsDto {

    private Long historyId;
    private Long dictionaryId;
    private String term;
    private String definition;
    private String description;
    private Boolean isBookmarked;
}
