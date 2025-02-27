package com.onedreamus.project.thisismoney.model.dto.backOffice;

import com.onedreamus.project.thisismoney.model.entity.DraftNews;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class DraftNewsResponse {

    private Integer id;
    private NewsContent newsContent;
    private LocalDate scheduledAt;
    private LocalDateTime createdAt;

    public static DraftNewsResponse from(DraftNews draftNews) {
        return DraftNewsResponse.builder()
                .id(draftNews.getId())
                .newsContent(draftNews.getNewsContent())
                .scheduledAt(draftNews.getScheduledAt())
                .createdAt(draftNews.getCreatedAt())
                .build();
    }

}
