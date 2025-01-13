package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NewsDictionaryScrapResponse {

    private Integer scrapCnt;
    private List<DictionaryNewsDto> dictionaryScraps;

    public static NewsDictionaryScrapResponse from(List<DictionaryNewsDto>  dictionaryScraps) {
        return NewsDictionaryScrapResponse.builder()
                .scrapCnt(dictionaryScraps.size())
                .dictionaryScraps(dictionaryScraps)
                .build();
    }
}
