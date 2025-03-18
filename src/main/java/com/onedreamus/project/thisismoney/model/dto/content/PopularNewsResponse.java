package com.onedreamus.project.thisismoney.model.dto.content;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsView;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PopularNewsResponse {

    private String title;
    private Integer viewCount;
    private Integer newsId;
    private LocalDate createdAt;
    private String thumbnailUrl;
    private List<String> tags;

    public static PopularNewsResponse from(News news, int totalViewCount, List<String> tags) {
        return PopularNewsResponse.builder()
                .title(news.getTitle())
                .viewCount(totalViewCount)
                .newsId(news.getId())
                .createdAt(news.getCreatedAt().toLocalDate())
                .thumbnailUrl(news.getThumbnailUrl())
                .tags(tags)
                .build();
    }

}
