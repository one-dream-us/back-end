package com.onedreamus.project.bank.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DictionaryScrapResponse {

    private Integer scrapCnt;
    private List<DictionaryScrapDto> dictionaryScraps;

    public static DictionaryScrapResponse from(List<DictionaryScrapDto> dtos) {
        return DictionaryScrapResponse.builder()
                .scrapCnt(dtos.size())
                .dictionaryScraps(dtos)
                .build();
    }
}
