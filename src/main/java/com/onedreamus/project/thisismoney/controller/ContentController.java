package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.dto.content.PopularNewsResponse;
import com.onedreamus.project.thisismoney.service.NewsService;
import com.onedreamus.project.thisismoney.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contents")
@RequiredArgsConstructor
@Tag(name = "contents", description = "콘텐츠 관련 API")
public class ContentController {

    private final NewsService newsService;
    private final ReviewService reviewService;

    // -----------[ News Contents ]---------

    @GetMapping("/news")
    @Operation(summary = "뉴스 콘텐츠 목록 조회", description = "여러 개의 뉴스 목록을 가져옵니다. 최신 뉴스부터 조회 할 수 있습니다. 'cursor'를 사용해 마지막으로 본 뉴스 이후부터 조회할 수 있습니다.")
    public ResponseEntity<CursorResult<NewsListResponse>> getNewsList(
            @Parameter(description = "마지막으로 조회된 콘텐츠의 ID (첫 페이지 조회 시 null)",
                    example = "123")
            @RequestParam(required = false) Integer cursor,
            @Parameter(description = "한 번에 가져올 콘텐츠의 개수",
                    example = "10",
                    schema = @Schema(minimum = "1", maximum = "100"))
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.getNewList(cursor, size));
    }

    @GetMapping("/news/{newsId}")
    @Operation(summary = "뉴스 상세페이지 조회", description = "뉴스 상세페이지로 이동하여 학습을 합니다.")
    public ResponseEntity<NewsDetailResponse> getNewsDetail(@PathVariable Integer newsId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        NewsDetailResponse newsDetailResponse;
        if (userDetails == null) {
            newsDetailResponse = newsService.getNewsDetail(newsId);
        } else {
            newsDetailResponse = newsService.getNewsDetail(newsId, userDetails.getUser());
        }
        return ResponseEntity.ok(newsDetailResponse);
    }

    @GetMapping("/news/latest")
    @Operation(summary = "가장 최근 콘텐츠 조회", description = "가장 최근에 업로드된 콘텐츠 1개를 가져옵니다.")
    public ResponseEntity<NewsListResponse> getLatestNews() {
        return ResponseEntity.ok(newsService.getLatestNews());
    }

    @PostMapping("/news/{newsId}/review")
    @Operation(summary = "뉴스 콘텐츠 리뷰", description = "뉴스 콘텐츠 별 리뷰를 남길 수 있습니다.")
    public ResponseEntity<String> reviewNews(@PathVariable Integer newsId, @RequestBody ReviewRequest reviewRequest ) throws IOException {
        reviewService.reviewNews(reviewRequest, newsId);
        return ResponseEntity.ok("리뷰 등록 완료");
    }

    @GetMapping("/news/popular")
    @Operation(summary = "인기 콘텐츠 조회", description = "인기 콘텐츠를 size 만큼 조회 합니다.")
    public ResponseEntity<List<PopularNewsResponse>> getPopularNews(@RequestParam Integer size) {
        List<PopularNewsResponse> response = newsService.getPopularNews(size);
        return ResponseEntity.ok(response);
    }
}
