package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.dto.bookmark.BookmarkRequest;
import com.onedreamus.project.thisismoney.model.dto.history.HistoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    @PostMapping
    @Operation(summary = "히스토리 추가", description = "용어 ID 값을 통해 히스토리에 저장합니다.")
    public ResponseEntity<String> addHistory(
            @RequestBody @Valid HistoryRequest historyRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {

        return ResponseEntity.ok("히스토리 추가 성공");
    }

}
