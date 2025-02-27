package com.onedreamus.project.thisismoney.model.dto;

import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ScheduledNewsDetailResponse {


    private String title;
    private String newsAgency; // 언론사
    private String fullSentence; // 전체 내용 -> 각 문장들을 함친 내용
    private String link; // 원문 링크
    private List<DictionaryDescriptionDto> descriptions; // 각 문장에 대한 설명
    private LocalDate scheduledAt;

    public static ScheduledNewsDetailResponse from(ScheduledNews scheduledNews) {
        NewsContent request = scheduledNews.getNewsContent();

        return ScheduledNewsDetailResponse.builder()
                .title(request.getTitle())
                .newsAgency(request.getNewsAgency())
                .fullSentence(request.getDictionarySentenceList().stream()
                        .map(DictionarySentenceRequest::getSentenceValue)
                        .collect(Collectors.joining()))
                .descriptions(request.getDictionarySentenceList().stream()
                        .map(DictionaryDescriptionDto::from)
                        .toList())
                .link(request.getOriginalLink())
                .scheduledAt(scheduledNews.getScheduledAt())
                .build();
    }

    public static ScheduledNewsDetailResponse from(String title, String newsAgency, String fullSentence,
                                                   String link, List<DictionaryDescriptionDto> dictionaryDescriptionDtos,
                                                   LocalDate scheduledAt) {
        return ScheduledNewsDetailResponse.builder()
                .title(title)
                .newsAgency(newsAgency)
                .fullSentence(fullSentence)
                .descriptions(dictionaryDescriptionDtos)
                .link(link)
                .scheduledAt(scheduledAt)
                .build();
    }
}
