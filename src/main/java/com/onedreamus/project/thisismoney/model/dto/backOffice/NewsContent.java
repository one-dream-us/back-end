package com.onedreamus.project.thisismoney.model.dto.backOffice;

import com.onedreamus.project.thisismoney.model.dto.DictionarySentenceRequest;
import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewsContent {

    private String title; // 뉴스 제목
    private String thumbnailUrl; // 썸네일 URL
    private String originalLink; // 기사 원본 링크
    private String newsAgency; // 뉴스 업로드한 에이전시
    private List<DictionarySentenceRequest> dictionarySentenceList;

    public static NewsContent from(NewsRequest newsRequest, String thumbnailUrl,
                                   List<DictionarySentenceRequest> dictionarySentenceRequests) {
        return NewsContent.builder()
            .title(newsRequest.getTitle())
            .thumbnailUrl(thumbnailUrl)
            .originalLink(newsRequest.getOriginalLink())
            .newsAgency(newsRequest.getNewsAgency())
            .dictionarySentenceList(dictionarySentenceRequests)
            .build();
    }

    public static NewsContent from(DraftNewsRequest draftNewsRequest, String thumbnailUrl,
                                   List<DictionarySentenceRequest> dictionarySentenceRequests) {
        return NewsContent.builder()
                .title(draftNewsRequest.getTitle())
                .thumbnailUrl(thumbnailUrl)
                .originalLink(draftNewsRequest.getOriginalLink())
                .newsAgency(draftNewsRequest.getNewsAgency())
                .dictionarySentenceList(dictionarySentenceRequests)
                .build();
    }
}
