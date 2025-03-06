package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/history")
@RequiredArgsConstructor
@Tag(name = "history", description = "히스토리 관련 API")
public class HistoryController {

    private final HistoryService historyService;

    @Operation(summary = "히스토리 추가", description = "특정 용어를 히스토리에 추가합니다. 'dictionaryId'로 용어를 선택해 스크랩할 수 있습니다.")
    @PostMapping("/dictionaries/{dictionaryId}")
    public ResponseEntity<String> addHistory(
        @PathVariable("dictionaryId") Long dictionaryId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        historyService.addHistory(dictionaryId, user);
        return ResponseEntity.ok("용어가 스크랩되었습니다.");
    }

    @Operation(summary = "히스토리 목록 조회", description = "히스토리 내 모든 용어 목록을 확인할 수 있습니다.")
    @GetMapping("/dictionaries")
    public ResponseEntity<DictionaryHistoryResponse> getHistoryList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        DictionaryHistoryResponse newsDictionaryScrapDtos = historyService.getHistoryList(user);
        return ResponseEntity.ok(newsDictionaryScrapDtos);
    }

//
//    @Operation(summary = "콘텐츠 스크랩하기", description = "콘텐츠를 스크랩에 추가합니다. 'contentId'로 특정 콘텐츠를 선택해 스크랩할 수 있습니다.")
//    @PostMapping("/contents/{contentId}")
//    public ResponseEntity<String> scrapContent(
//        @PathVariable("contentId") Long contentId,
//        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        historyService.scrapContent(contentId, user);
//        return ResponseEntity.ok("콘텐츠가 스크랩되었습니다.");
//    }
//
//    @Operation(summary = "스크랩한 콘텐츠 조회", description = "내가 스크랩한 모든 콘텐츠 목록을 확인할 수 있습니다.")
//    @GetMapping("/contents")
//    public ResponseEntity<ContentScrapResponse> getContentScrapped(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        ContentScrapResponse contentScrapResponse = historyService.getContentScrapped(user);
//        return ResponseEntity.ok(contentScrapResponse);
//    }
//
//    @Operation(summary = "스크랩한 콘텐츠 삭제", description = "스크랩한 콘텐츠를 삭제할 수 있습니다. 'contentScrapId'로 삭제할 콘텐츠를 선택합니다.")
//    @DeleteMapping("/contents/{contentScrapId}")
//    public ResponseEntity<String> deleteContentScrapped(
//        @PathVariable("contentScrapId") Integer contentScrapId,
//        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        historyService.deleteContentScrapped(contentScrapId, user);
//        return ResponseEntity.ok("스크랩된 콘텐츠가 삭제되었습니다.");
//    }
//
//    @Deprecated
//    @Operation(summary = "용어 스크랩하기", description = "특정 용어를 스크랩에 추가합니다. 'dictionaryId'로 용어를 선택해 스크랩할 수 있습니다.")
//    @PostMapping("/dictionaries/{dictionaryId}/contents/{contentId}")
//    public ResponseEntity<String> scrapTerm(
//        @PathVariable("dictionaryId") Long dictionaryId,
//        @PathVariable("contentId") Long contentId,
//        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        historyService.scrapDictionary(dictionaryId, contentId, user);
//        return ResponseEntity.ok("용어가 스크랩되었습니다.");
//    }

////
//    @Operation(summary = "사용자의 전체 스크랩 수 조회", description = "내가 스크랩한 콘텐츠와 용어의 총 개수를 조회할 수 있습니다.")
//    @GetMapping("/total")
//    public ResponseEntity<TotalScarpCntDto> getTotalScrapCnt(@AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        TotalScarpCntDto totalScarpCntDto = historyService.getTotalScarpCnt(user);
//        return ResponseEntity.ok(totalScarpCntDto);
//    }
//
//    @Deprecated
//    @Operation(summary = "사용자가 스크랩한 콘텐츠 수 조회", description = "사용자가 스크랩한 콘텐츠의 총 개수를 조회할 수 있습니다.")
//    @GetMapping("/contents/count")
//    public ResponseEntity<ContentScrapCntDto> getContentScrapCnt(
//        @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        ContentScrapCntDto contentScrapCntDto = historyService.getContentScrapCnt(user);
//        return ResponseEntity.ok(contentScrapCntDto);
//    }

}
