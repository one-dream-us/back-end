package com.onedreamus.project.thisismoney.model.dto.backOffice;

import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ScheduledNewsResponse {

    private Integer id;
    private NewsContent newsContent;
    private LocalDate scheduledAt;

    public static ScheduledNewsResponse from(ScheduledNews scheduledNews) {
        return ScheduledNewsResponse.builder()
            .id(scheduledNews.getId())
            .newsContent(scheduledNews.getNewsContent())
            .scheduledAt(scheduledNews.getScheduledAt())
            .build();
    }
}
