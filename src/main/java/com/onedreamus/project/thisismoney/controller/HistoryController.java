package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.dto.history.HistoryRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "여러 히스토리 추가", description = "여러 용어를 한번에 히스토리에 추가합니다. ")
    @PostMapping("/dictionaries")
    public ResponseEntity<String> addHistory(
        @RequestBody HistoryRequest historyRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        historyService.addHistoryList(historyRequest.getDictionaryIds(), user);
        return ResponseEntity.ok("용어가 스크랩되었습니다.");
    }

    @Operation(summary = "히스토리 목록 조회", description = "히스토리 내 모든 용어 목록을 확인할 수 있습니다.")
    @GetMapping("")
    public ResponseEntity<DictionaryHistoryResponse> getHistoryList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        DictionaryHistoryResponse newsDictionaryScrapDtos = historyService.getHistoryList(user);
        return ResponseEntity.ok(newsDictionaryScrapDtos);
    }
}
