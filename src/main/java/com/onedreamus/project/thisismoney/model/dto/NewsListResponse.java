package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.News;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class NewsListResponse {

    private Long newsId;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private String viewCount;
    private List<String> tags;

    public static NewsListResponse from(News news, String totalViewCount, List<String> tags) {
        return NewsListResponse.builder()
            .newsId(Long.valueOf(news.getId()))
            .title(news.getTitle())
            .thumbnailUrl(news.getThumbnailUrl())
            .createdAt(news.getCreatedAt())
            .viewCount(totalViewCount)
            .tags(tags)
            .build();
    }
}
