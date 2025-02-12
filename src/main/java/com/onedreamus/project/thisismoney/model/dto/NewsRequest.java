package com.onedreamus.project.thisismoney.model.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsRequest {

    @NotBlank(message = "뉴스 제목은 필수 값 입니다.")
    private String title; // 뉴스 제목

    @NotBlank(message = "썸네일 URL은 필수 값 입니다.")
    private String thumbnailUrl; // 썸네일 URL

    @NotBlank(message = "원본 링크는 필수 값 입니다.")
    private String originalLink; // 기사 원본 링크

    @NotBlank(message = "에이전시 값은 필수 값 입니다.")
    private String newsAgency; // 뉴스 업로드한 에이전시

    @Valid
    @NotEmpty(message = "용어, 문장 값은 필수 입니다.")
    private List<DictionarySentenceRequest> dictionarySentenceList;

}
