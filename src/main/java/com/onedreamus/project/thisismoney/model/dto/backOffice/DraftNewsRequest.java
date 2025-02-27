package com.onedreamus.project.thisismoney.model.dto.backOffice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NotBlank
@Getter
@Setter
public class DraftNewsRequest {

    /**
     * 임시 저장이기 때문에 모든 값이 있을 수 도 있고, 없을 수도 있음.
     */
    private String title; // 뉴스 제목
    private String originalLink; // 기사 원본 링크
    private String newsAgency; // 뉴스 업로드한 에이전시
    private LocalDate scheduledAt; // 예약 업로드 날짜
    private Integer draftNewsId; // 기존 임시저장된 콘텐츠 였는지 확인 하는 용도 -> 기존 임시저장됐던 콘텐츠 일 경우 수정 로직을 수행하도록 해야함


}
