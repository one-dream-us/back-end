package com.onedreamus.project.thisismoney.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsRequest {

    private String title; // 뉴스 제목
    private String thumbnailUrl; // 썸네일 URL
    private String originalLink; // 기사 원본 링크
    private String newsAgency; // 뉴스 업로드한 에이전시
    private List<DictionarySentenceRequest> dictionarySentenceList;

}
