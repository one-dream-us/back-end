package com.onedreamus.project.bank.model.dto;

import com.onedreamus.project.bank.model.entity.Term;
import com.onedreamus.project.bank.model.entity.TermScrap;
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
public class TermScrapDto {

    private Integer scrapId;
    private Term term;

    public static TermScrapDto from(TermScrap termScrap) {
        return TermScrapDto.builder()
            .scrapId(termScrap.getId())
            .term(termScrap.getTerm())
            .build();
    }
}
