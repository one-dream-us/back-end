package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.News;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsResponse {

    private Integer id;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private String newsAgency;
    private String link;

    public static NewsResponse from(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .thumbnailUrl(news.getThumbnailUrl())
                .createdAt(news.getCreatedAt())
                .newsAgency(news.getNewsAgency())
                .link(news.getLink())
                .build();
    }

}
