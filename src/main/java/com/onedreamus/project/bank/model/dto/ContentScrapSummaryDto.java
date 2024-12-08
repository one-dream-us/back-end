package com.onedreamus.project.bank.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContentScrapSummaryDto {

    private Long scrapId;
    private LocalDateTime createdAt;
    private Long contentId;
    private String thumbnailUrl;
    private String contentTitle;
    private String summaryText;
}
