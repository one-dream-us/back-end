package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.BookmarkResponse;
import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final NoteService noteService;

    @Operation(summary = "북마크 조회", description = "북마크를 조회합니다.")
    @GetMapping("")
    public ResponseEntity<BookmarkResponse> getBookmarkList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        BookmarkResponse bookmarkResponse = noteService.getBookmarkList(user);
        return ResponseEntity.ok(bookmarkResponse);
    }

    @Operation(summary = "용어 북마크에 추가", description = "dictionaryId로 용어를 북마크에 추가합니다. ")
    @PostMapping("/dictionaries/{dictionaryId}")
    public ResponseEntity<String> addBookmark(
        @PathVariable Long dictionaryId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        noteService.addBookmark(dictionaryId, userDetails.getUser());
        return ResponseEntity.ok("북마크에 추가되었습니다.");
    }

    @Operation(summary = "북마크 취소", description = "북마크를 취소합니다.")
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<String> deleteBookmark(
        @PathVariable Long bookmarkId, @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        noteService.deleteBookmark(bookmarkId, userDetails.getUser());
        return ResponseEntity.ok("북마크에서 삭제되었습니다.");
    }

}
