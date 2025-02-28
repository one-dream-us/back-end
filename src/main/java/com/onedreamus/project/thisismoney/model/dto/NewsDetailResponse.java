package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NewsDetailResponse {

    private String title;
    private String newsAgency; // 언론사
    private String fullSentence; // 전체 내용 -> 각 문장들을 함친 내용
    private String link; // 원문 링크
    private String thumbnailUrl; // 써네일 이미지 URL
    private List<DictionaryDescriptionDto> descriptions; // 각 문장에 대한 설명

    public static NewsDetailResponse from(News news, String fullSentence,
        List<DictionaryDescriptionDto> descriptions) {
        return NewsDetailResponse.builder()
            .title(news.getTitle())
            .newsAgency(news.getNewsAgency())
            .fullSentence(fullSentence)
            .thumbnailUrl(news.getThumbnailUrl())
            .descriptions(descriptions)
            .link(news.getLink())
            .build();
    }
}
