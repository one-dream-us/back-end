package com.onedreamus.project.bank.model.dto;

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
public class ContentScrapTagDto {

    private Long scrapId;
    private Long contentId;
    private String tagValue;
    private Integer sequence;


}
