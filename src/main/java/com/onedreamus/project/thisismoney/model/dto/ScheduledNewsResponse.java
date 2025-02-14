package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import com.onedreamus.project.thisismoney.service.ScheduledNewsService;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ScheduledNewsResponse {

    private Integer id;
    private ScheduledNewsRequest newsRequest;
    private LocalDate scheduledAt;

    public static ScheduledNewsResponse from(ScheduledNews scheduledNews) {
        return ScheduledNewsResponse.builder()
            .id(scheduledNews.getId())
            .newsRequest(scheduledNews.getScheduledNewsRequest())
            .scheduledAt(scheduledNews.getScheduledAt())
            .build();
    }
}
