package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.dto.backOffice.DraftNewsDetailResponse;
import com.onedreamus.project.thisismoney.model.dto.backOffice.DraftNewsRequest;
import com.onedreamus.project.thisismoney.model.dto.backOffice.DraftNewsResponse;
import com.onedreamus.project.thisismoney.model.dto.backOffice.ScheduledNewsResponse;
import com.onedreamus.project.thisismoney.service.AgencyService;
import com.onedreamus.project.thisismoney.service.DraftNewsService;
import com.onedreamus.project.thisismoney.service.NewsService;
import com.onedreamus.project.thisismoney.service.ScheduledNewsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/back-office")
@RequiredArgsConstructor
public class BackOfficeController {

    private final ScheduledNewsService scheduledNewsService;
    private final NewsService newsService;
    private final AgencyService agencyService;


    /*
    ======== [News] ========
     */

    @PostMapping("/contents/news")
    @Operation(summary = "뉴스 콘텐츠 즉시 업로드", description = "API가 호출되면 즉시 뉴스 콘텐츠 업로드 동작을 수행합니다.")
    public ResponseEntity<String> uploadNews(
            @RequestPart @Valid NewsRequest newsRequest,
            @RequestPart @Valid List<DictionarySentenceRequest> dictionarySentenceList,
            @RequestPart MultipartFile thumbnailImage) {
        newsService.uploadNews(newsRequest, thumbnailImage, dictionarySentenceList);
        return ResponseEntity.ok("콘텐츠 등록 완료");
    }

    @GetMapping("/contents/news")
    @Operation(summary = "업로드 된 뉴스 콘텐츠 리스트 조회", description = "페이지네이션 된 업로드 뉴스 콘텐츠 리스트를 조회합니다.")
    public ResponseEntity<Page<NewsResponse>> getNewsList(
            @PageableDefault Pageable pageable
    ) {
        Page<NewsResponse> newsResponsePage = newsService.getNewsList(pageable);
        return ResponseEntity.ok(newsResponsePage);
    }

    @GetMapping("/contents/news/{newsId}")
    @Operation(summary = "업로드 된 뉴스 콘텐츠 상세 데이터 조회", description = "ID 값으로 업로드 된 뉴스 콘텐츠 조회")
    public ResponseEntity<NewsDetailResponse> getNewsDetail(
            @PathVariable("newsId") Integer newsId) {
        NewsDetailResponse newsDetailResponse = newsService.getNewsDetailWithoutViewIncrease(newsId);
        return ResponseEntity.ok(newsDetailResponse);
    }


    /*
    ======== [Scheduled News] ========
     */


    @PostMapping("/contents/news/scheduled/{scheduledAt}")
    @Operation(summary = "뉴스 콘텐츠 업로드 예약", description = "뉴스 콘텐츠 업로드 날짜를 설정하고 예약 합니다.")
    public ResponseEntity<String> scheduleContentUpload(
            @RequestPart @Valid NewsRequest newsRequest,
            @RequestPart @Valid List<DictionarySentenceRequest> dictionarySentenceList,
            @RequestPart MultipartFile thumbnailImage,
            @PathVariable("scheduledAt") LocalDate scheduledAt) {
        scheduledNewsService.scheduleUploadNews(newsRequest, thumbnailImage, dictionarySentenceList, scheduledAt);
        return ResponseEntity.ok("콘텐츠 등록 완료");
    }

    @PutMapping("/contents/news/scheduled/{scheduledNewsId}/{scheduledAt}")
    @Operation(summary = "예약 콘텐츠 수정", description = "업로드 예약된 콘텐츠 수정 상세 데이터를 수정합니다.")
    public ResponseEntity<String> updateScheduledContent(
            @RequestPart @Valid NewsRequest newsRequest,
            @RequestPart @Valid List<DictionarySentenceRequest> dictionarySentenceList,
            @RequestPart(required = false) MultipartFile thumbnailImage,
            @PathVariable("scheduledAt") LocalDate scheduledAt,
            @PathVariable("scheduledNewsId") Integer scheduledNewsId) {
        scheduledNewsService.updateScheduledNews(newsRequest, thumbnailImage, dictionarySentenceList, scheduledAt, scheduledNewsId);
        return ResponseEntity.ok("업데이트 완료");
    }

    @GetMapping("/contents/news/scheduled")
    @Operation(summary = "예약 뉴스 업로드 리스트 조회", description = "페이지네이션 된 예약한 뉴스 업로드 리스트를 조회합니다.")
    public ResponseEntity<Page<ScheduledNewsResponse>> getScheduledNewsList(
            @PageableDefault Pageable pageable) {
        Page<ScheduledNewsResponse> scheduledNewsPage = scheduledNewsService.getScheduledNewsList(
                pageable);
        return ResponseEntity.ok(scheduledNewsPage);
    }

    @GetMapping("/contents/news/scheduled/{scheduledNewsId}")
    @Operation(summary = "업로드 예약 된 뉴스 콘텐츠 상세 데이터 조회", description = "ID 값으로 업로드 예약 된 뉴스 콘텐츠 조회")
    public ResponseEntity<ScheduledNewsDetailResponse> getScheduledNewsDetail(
            @PathVariable("scheduledNewsId") Integer newsId) {
        ScheduledNewsDetailResponse scheduledNewsDetail = scheduledNewsService.getScheduledNewsDetail(newsId);
        return ResponseEntity.ok(scheduledNewsDetail);
    }

    @GetMapping("/agency/{keyword}")
    @Operation(summary = "뉴스사 검색", description = "keyword를 포함하는 모든 뉴스사를 조회합니다.")
    public ResponseEntity<List<AgencySearch>> searchAgency(
            @PathVariable("keyword") String keyword) {
        List<AgencySearch> agencySearches = agencyService.searchAgency(keyword);
        return ResponseEntity.ok(agencySearches);
    }


    /*
    ======== [Draft News] ========
     */


    private final DraftNewsService draftNewsService;

    @PostMapping("/contents/news/drafts")
    @Operation(summary = "콘텐츠 임시 저장 및 수정", description = "작성 중이던 콘텐츠를 임시 저장 합니다.")
    public ResponseEntity<String> saveDrafts(
            @RequestPart DraftNewsRequest draftNewsRequest,
            @RequestPart List<DictionarySentenceRequest> dictionarySentenceList,
            @RequestPart(required = false) MultipartFile thumbnailImage) {
        draftNewsService.createDraft(draftNewsRequest, dictionarySentenceList, thumbnailImage);
        return ResponseEntity.ok("임시 저장 완료");
    }

    @GetMapping("/contents/news/drafts")
    @Operation(summary = "임시 저장 콘텐츠 리스트 조회", description = "페이지네이션 된 임시 저장 항목 리스트를 조회합니다.")
    public ResponseEntity<Page<DraftNewsResponse>> getDraftList(@PageableDefault Pageable pageable) {
        Page<DraftNewsResponse> draftNewsPage = draftNewsService.getDraftList(pageable);
        return ResponseEntity.ok(draftNewsPage);
    }

    @GetMapping("/contents/news/drafts/{draftId}")
    @Operation(summary = "임시 저장 콘텐츠 상세 데이터 조회", description = "ID 값을 통해 특정 임시 저장 콘텐츠를 조회합니다.")
    public ResponseEntity<DraftNewsDetailResponse> getDraftNews(@PathVariable("draftId") Integer draftId) {
        DraftNewsDetailResponse draftNewsDetailResponse = draftNewsService.getDraftNews(draftId);
        return ResponseEntity.ok(draftNewsDetailResponse);
    }

    @DeleteMapping("/contents/news/drafts/{draftId}")
    @Operation(summary = "임시 저장 콘텐츠 삭제", description = "ID 값을 통해 특정 임시 저장 콘텐츠를 삭제합니다.")
    public ResponseEntity<String> deleteDraftNews(@PathVariable("draftId") Integer draftId) {
        draftNewsService.deleteDraftNews(draftId);
        return ResponseEntity.ok("삭제 완료");
    }
}
