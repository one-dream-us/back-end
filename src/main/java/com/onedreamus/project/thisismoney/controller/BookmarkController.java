package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.dto.bookmark.BookmarkRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {


    @PostMapping
    @Operation(summary = "북마크 추가", description = "")
    public ResponseEntity<String> addBookmark(
            @RequestBody @Valid BookmarkRequest bookmarkRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        return ResponseEntity.ok("북마크 성공");
    }

}
