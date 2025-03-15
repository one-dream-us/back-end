package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.google.GoogleSheetsService;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.ReviewRequest;
import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsReview;
import com.onedreamus.project.thisismoney.repository.NewsReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final NewsReviewRepository newsReviewRepository;
    private final NewsService newsService;
    private final GoogleSheetsService googleSheetsService;

    /**
     * <p>콘텐츠 리뷰 등록</p>
     * newsId를 통해 뉴스 콘텐츠에 대한 리뷰 등록
     * @param reviewRequest
     * @param newsId
     */
    @Transactional
    public void reviewNews(ReviewRequest reviewRequest, Integer newsId) throws IOException {
        News news = newsService.getNewsById(newsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        newsReviewRepository.save(NewsReview.from(news, reviewRequest.getScore(), reviewRequest.getValue()));

        // 구글 스프레드 시트에 데이터 삽입
        googleSheetsService.insertReviewData(LocalDate.now(), reviewRequest.getValue(), reviewRequest.getScore());
    }

}
