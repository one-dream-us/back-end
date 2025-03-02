package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.s3.ImageCategory;
import com.onedreamus.project.global.s3.S3Uploader;
import com.onedreamus.project.thisismoney.exception.BackOfficeException;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.dto.backOffice.NewsContent;
import com.onedreamus.project.thisismoney.model.dto.backOffice.ScheduledNewsResponse;
import com.onedreamus.project.thisismoney.model.entity.DictionarySentence;
import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import com.onedreamus.project.thisismoney.repository.ScheduledNewsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledNewsService {

    private final ScheduledNewsRepository scheduledNewsRepository;
    private final S3Uploader s3Uploader;
    private final NewsService newsService;

    /**
     * <p>예약 뉴스 콘텐츠 등록</p>
     * 매일 새벽 6시에 업로드 되도록 설정됨.
     */
    public void scheduleUploadNews(NewsRequest newsRequest, MultipartFile thumbnailImage,
                                   List<DictionarySentenceRequest> dictionarySentenceRequests, LocalDate scheduledAt) {
        boolean isDateDuplicated = scheduledNewsRepository.existsByScheduledAt(scheduledAt);
        if (isDateDuplicated) {
            throw new BackOfficeException(ErrorCode.DATE_DUPLICATION);
        }
        String thumbnailUrl = s3Uploader.uploadMultipartFileByStream(
                thumbnailImage, ImageCategory.THUMBNAIL);
        NewsContent newsContent = NewsContent.from(newsRequest,
                thumbnailUrl, dictionarySentenceRequests);
        scheduledNewsRepository.save(
                ScheduledNews.from(newsContent, scheduledAt));
    }


    public Optional<ScheduledNews> getScheduledNewsByScheduledAt(LocalDate date) {
        return scheduledNewsRepository.findByScheduledAt(date);
    }

    public Page<ScheduledNewsResponse> getScheduledNewsList(Pageable pageable) {
        return scheduledNewsRepository.findAllByOrderByScheduledAtAsc(pageable)
                .map(ScheduledNewsResponse::from);
    }

    public void deleteScheduledNewsById(int scheduledNewsId) {
        scheduledNewsRepository.deleteById(scheduledNewsId);
    }

    /**
     * <p>[업로드 예약 콘텐츠 상세 조회]</p>
     * 업로드 예약 된 콘텐츠의 상세 데이터를 조회합니다.
     *
     * @param scheduledNewsId
     * @return
     */
    public ScheduledNewsDetailResponse getScheduledNewsDetail(Integer scheduledNewsId) {
        ScheduledNews scheduledNews = scheduledNewsRepository.findById(scheduledNewsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        List<DictionaryDescriptionDto> dictionaryDescriptionDtos = scheduledNews.getNewsContent()
                .getDictionarySentenceList().stream()
                .map(newsService::highlightDefinitionAndSentence)
                .toList();

        String fullHighlightedSentence = dictionaryDescriptionDtos.stream()
                .map(DictionaryDescriptionDto::getSentence)
                .collect(Collectors.joining());

        NewsContent newsContent = scheduledNews.getNewsContent();

        return ScheduledNewsDetailResponse.from(
                newsContent,
                fullHighlightedSentence,
                dictionaryDescriptionDtos,
                scheduledNews.getScheduledAt()
        );
    }

    /**
     * 예약 콘텐츠 상세 데이터 수정
     *
     * @param newsRequest
     * @param thumbnailImage
     * @param dictionarySentenceList
     * @param scheduledAt
     * @param scheduledNewsId
     */
    public void updateScheduledNews(
            NewsRequest newsRequest, MultipartFile thumbnailImage,
            List<DictionarySentenceRequest> dictionarySentenceList, LocalDate scheduledAt,
            Integer scheduledNewsId) {

        // 예약된 뉴스 조회
        ScheduledNews scheduledNews = scheduledNewsRepository.findById(scheduledNewsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        NewsContent newsContent = scheduledNews.getNewsContent();

        // 써네일 이미지 업데이트
        String updatedThumbnailUrl = handleThumbnailUpdate(newsContent.getThumbnailUrl(), thumbnailImage);
        newsContent.setThumbnailUrl(updatedThumbnailUrl);

        // 데이터 수정
        updateNewsContent(newsContent, newsRequest, dictionarySentenceList);

        // 예약 시간 업데이트
        scheduledNews.setScheduledAt(scheduledAt);
        scheduledNews.setNewsContent(newsContent);

        scheduledNewsRepository.save(scheduledNews);
    }

    /**
     * News Content 업데이트
     * @param newsContent
     * @param newsRequest
     * @param dictionarySentenceList
     */
    private void updateNewsContent(NewsContent newsContent, NewsRequest newsRequest, List<DictionarySentenceRequest> dictionarySentenceList) {
        newsContent.setNewsAgency(newsRequest.getNewsAgency());
        newsContent.setTitle(newsRequest.getTitle());
        newsContent.setOriginalLink(newsRequest.getOriginalLink());
        newsContent.setDictionarySentenceList(dictionarySentenceList);
    }

    /**
     * <p>✅ 썸네일 이미지 변경 로직</p>
     * <li> 새 이미지가 없으면 기존 이미지 유지</li>
     * <li> 새 이미지가 업로드 되면 기존 이미지 삭제 후 새 이미지 업로드</li>
     */
    private String handleThumbnailUpdate(String existingThumbnailUrl, MultipartFile newThumbnail) {
        // 1. 새로운 이미지가 없고, 기존 이미지 URL이 그대로라면 변경 없음
        if (newThumbnail == null || newThumbnail.isEmpty()) {
            return existingThumbnailUrl;
        }

        // 2. 새로운 이미지가 업로드된 경우, 기존 이미지 삭제
        if (existingThumbnailUrl != null && !existingThumbnailUrl.isEmpty()) {
            s3Uploader.deleteImageByUrl(existingThumbnailUrl);
        }

        return s3Uploader.uploadMultipartFileByStream(newThumbnail, ImageCategory.THUMBNAIL);
    }
}
