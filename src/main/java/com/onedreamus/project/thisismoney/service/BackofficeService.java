package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.DictionarySentenceRequest;
import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackofficeService {

    private final DraftNewsService draftNewsService;
    private final ScheduledNewsService scheduledNewsService;
    private final NewsService newsService;

    /**
     * 뉴스 콘텐츠 예약 업로드 (임시 저장 데이터 삭제 포함)
     *
     * @param draftNewsId 삭제할 임시 뉴스 ID (선택적)
     * @param newsRequest 뉴스 요청 데이터
     * @param thumbnailImage 썸네일 이미지
     * @param dictionarySentenceList 용어 문장 리스트
     * @param scheduledAt 예약할 날짜
     */
    @Transactional
    public void uploadScheduledNews(Integer draftNewsId, NewsRequest newsRequest,
        MultipartFile thumbnailImage, List<DictionarySentenceRequest> dictionarySentenceList,
        LocalDate scheduledAt) {
        // 뉴스 콘텐츠 업로드 예약
        scheduledNewsService.scheduleUploadNews(newsRequest, thumbnailImage, dictionarySentenceList,
                scheduledAt);

        // 임시 저장 데이터에서 작성된 경우 임시저장 데이터 삭제
        deleteDraftIfExists(draftNewsId);
    }

    /**
     * 뉴스 콘텐츠 즉시 업로드 (임시 저장 데이터 삭제 포함)
     *
     * @param draftNewsId 삭제할 임시 뉴스 ID (선택적)
     * @param newsRequest 뉴스 요청 데이터
     * @param thumbnailImage 썸네일 이미지
     * @param dictionarySentenceList 용어 문장 리스트
     */
    @Transactional
    public void uploadNews(Integer draftNewsId, NewsRequest newsRequest,
        MultipartFile thumbnailImage,
        List<DictionarySentenceRequest> dictionarySentenceList) {
        // 임시 저장 데이터에서 작성된 경우 임시저장 데이터 삭제
        deleteDraftIfExists(draftNewsId);

        // 뉴스 콘텐츠 업로드
        newsService.uploadNews(newsRequest, thumbnailImage, dictionarySentenceList);
    }

    /**
     * 임시 저장 뉴스 데이터가 존재하는 경우 삭제
     *
     * @param draftNewsId 삭제할 임시 뉴스 ID (null이면 동작하지 않음)
     */
    private void deleteDraftIfExists(Integer draftNewsId){
        if (draftNewsId != null) {
            draftNewsService.deleteDraftNews(draftNewsId);
        }
    }
}
