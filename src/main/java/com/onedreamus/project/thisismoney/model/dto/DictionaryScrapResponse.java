package com.onedreamus.project.thisismoney.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryScrapResponse {

    private Integer scrapCnt;
    private List<DictionaryContentDto> dictionaryScraps;

    public static DictionaryScrapResponse from(List<DictionaryContentDto> dtos) {
        return DictionaryScrapResponse.builder()
            .scrapCnt(dtos.size())
            .dictionaryScraps(dtos)
            .build();
    }
}
