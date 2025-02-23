package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.s3.ImageCategory;
import com.onedreamus.project.global.s3.S3Uploader;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.DictionarySentence;
import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import com.onedreamus.project.thisismoney.repository.ScheduledNewsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * <p>예약 뉴스 콘텐츠 등록</p>
     * 매일 새벽 6시에 업로드 되도록 설정됨.
     */
    public void scheduleUploadNews(NewsRequest newsRequest, MultipartFile thumbnailImage,
                                   List<DictionarySentenceRequest> dictionarySentenceRequests, LocalDate scheduledAt) {
        String thumbnailUrl = s3Uploader.uploadMultipartFileByStream(
                thumbnailImage, ImageCategory.THUMBNAIL);
        ScheduledNewsRequest scheduledNewsRequest = ScheduledNewsRequest.from(newsRequest,
                thumbnailUrl, dictionarySentenceRequests);
        scheduledNewsRepository.save(
                ScheduledNews.from(scheduledNewsRequest, scheduledAt, thumbnailUrl));
    }


    public Optional<ScheduledNews> getScheduledNewsByScheduledAt(LocalDate date) {
        return scheduledNewsRepository.findByScheduledAt(date);
    }

    public Page<ScheduledNewsResponse> getScheduledNewsList(Pageable pageable) {
        return scheduledNewsRepository.findAllByOrderByScheduledAtAsc(pageable)
                .map(ScheduledNewsResponse::from);
    }

    public void deleteScheduledNews(ScheduledNews scheduledNews) {
        scheduledNewsRepository.delete(scheduledNews);
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

        return ScheduledNewsDetailResponse.from(scheduledNews);
    }
}
