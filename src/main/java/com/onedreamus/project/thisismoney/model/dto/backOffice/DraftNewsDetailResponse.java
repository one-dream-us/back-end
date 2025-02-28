package com.onedreamus.project.thisismoney.model.dto.backOffice;

import com.onedreamus.project.thisismoney.model.dto.DictionaryDescriptionDto;
import com.onedreamus.project.thisismoney.model.entity.DraftNews;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DraftNewsDetailResponse {

    private String title;
    private String newsAgency; // 언론사
    private String fullSentence; // 전체 내용 -> 각 문장들을 함친 내용
    private String link; // 원문 링크
    private String thumbnailUrl;
    private List<DictionaryDescriptionDto> descriptions; // 각 문장에 대한 설명
    private LocalDate scheduledAt;

    public static DraftNewsDetailResponse from(LocalDate scheduledAt, NewsContent newsContent, List<DictionaryDescriptionDto> dictionaryDescriptionDtos
            ,String fullSentence) {
        return DraftNewsDetailResponse.builder()
                .title(newsContent.getTitle())
                .newsAgency(newsContent.getNewsAgency())
                .fullSentence(fullSentence)
                .descriptions(dictionaryDescriptionDtos)
                .scheduledAt(scheduledAt)
                .build();
    }
}
