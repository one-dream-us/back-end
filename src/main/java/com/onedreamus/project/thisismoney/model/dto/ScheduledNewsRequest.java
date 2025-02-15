package com.onedreamus.project.thisismoney.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScheduledNewsRequest {

    private String title; // 뉴스 제목
    private String thumbnailUrl; // 썸네일 URL
    private String originalLink; // 기사 원본 링크
    private String newsAgency; // 뉴스 업로드한 에이전시
    private List<DictionarySentenceRequest> dictionarySentenceList;

    public static ScheduledNewsRequest from(NewsRequest newsRequest, String thumbnailUrl,
        List<DictionarySentenceRequest> dictionarySentenceRequests) {
        return ScheduledNewsRequest.builder()
            .title(newsRequest.getTitle())
            .thumbnailUrl(thumbnailUrl)
            .originalLink(newsRequest.getOriginalLink())
            .newsAgency(newsRequest.getNewsAgency())
            .dictionarySentenceList(dictionarySentenceRequests)
            .build();
    }
}
