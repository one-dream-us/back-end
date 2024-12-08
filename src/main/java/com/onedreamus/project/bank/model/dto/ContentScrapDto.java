package com.onedreamus.project.bank.model.dto;

import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.ScriptSummary;
import com.onedreamus.project.bank.model.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;
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
public class ContentScrapDto {

    private Long scrapId;
    private Long contentId;
    private String contentTitle;
    private String thumbnailUrl;
    private List<TagDto> tags;
    private String summaryText;
    private LocalDateTime createdAt; // 스크랩 날짜

}
