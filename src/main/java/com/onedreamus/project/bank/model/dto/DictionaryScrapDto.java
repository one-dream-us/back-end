package com.onedreamus.project.bank.model.dto;

import com.onedreamus.project.bank.model.entity.Dictionary;
import com.onedreamus.project.bank.model.entity.DictionaryScrap;
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
public class DictionaryScrapDto {

    private Long scrapId;
    private Dictionary dictionary;

    public static DictionaryScrapDto from(DictionaryScrap dictionaryScrap) {
        return DictionaryScrapDto.builder()
            .scrapId(dictionaryScrap.getId())
            .dictionary(dictionaryScrap.getDictionary())
            .build();
    }
}
