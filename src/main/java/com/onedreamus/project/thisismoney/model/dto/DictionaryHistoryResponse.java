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
public class DictionaryHistoryResponse {

    private Integer historyCnt;
    private List<DictionaryNewsDto> dictionaryHistory;

    public static DictionaryHistoryResponse from(List<DictionaryNewsDto> dictionaryHistory) {
        return DictionaryHistoryResponse.builder()
            .historyCnt(dictionaryHistory.size())
            .dictionaryHistory(dictionaryHistory)
            .build();
    }
}
