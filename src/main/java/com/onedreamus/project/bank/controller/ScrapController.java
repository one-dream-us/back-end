package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.ContentScrapCntDto;
import com.onedreamus.project.bank.model.dto.ContentScrapResponse;
import com.onedreamus.project.bank.model.dto.CustomUserDetails;
import com.onedreamus.project.bank.model.dto.DictionaryScrapCntDto;
import com.onedreamus.project.bank.model.dto.DictionaryScrapResponse;
import com.onedreamus.project.bank.model.dto.TotalScarpCntDto;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.service.ScrapService;
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
@RequestMapping("/api/v1/scraps")
@RequiredArgsConstructor
@Tag(name = "스크랩", description = "스크랩 관련 API")
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 콘텐츠 스크랩 API
     */
    @Operation(summary = "콘텐츠 스크랩 추가", description = "지정한 콘텐츠를 스크랩합니다.")
    @PostMapping("/contents/{contentId}")
    public ResponseEntity<String> scrapContent(
        @PathVariable("contentId") Integer contentId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        scrapService.scrapContent(contentId, user);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     *스크랩된 콘텐츠 전체 조회 API
     */
    @Operation(summary = "스크랩된 콘텐츠 조회", description = "사용자가 스크랩한 모든 콘텐츠를 조회합니다.")
    @GetMapping("/contents")
    public ResponseEntity<ContentScrapResponse> getContentScrapped(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        ContentScrapResponse contentScrapResponse = scrapService.getContentScrapped(user);
        return ResponseEntity.ok(contentScrapResponse);
    }

    /**
     * 스크랩된 콘텐츠 삭제 API
     */
    @Operation(summary = "콘텐츠 스크랩 삭제", description = "사용자가 스크랩한 콘텐츠를 삭제합니다.")
    @DeleteMapping("/contents/{contentScrapId}")
    public ResponseEntity<String> deleteContentScrapped(
        @PathVariable("contentScrapId") Integer contentScrapId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        scrapService.deleteContentScrapped(contentScrapId, user);
        return ResponseEntity.ok("스크랩 삭제 성공");
    }

    /**
     * 용어 스크랩 API
     */
    @Operation(summary = "용어 스크랩 추가", description = "지정한 용어를 스크랩합니다.")
    @PostMapping("/dictionaries/{dictionaryId}")
    public ResponseEntity<String> scrapTerm(
        @PathVariable("dictionaryId") Long dictionaryId,
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        scrapService.scrapDictionary(dictionaryId, user);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     * 스크랩된 용어 전체 조회 API
     */
    @Operation(summary = "스크랩된 용어 조회", description = "사용자가 스크랩한 모든 용어를 조회합니다.")
    @GetMapping("/dictionaries")
    public ResponseEntity<DictionaryScrapResponse> getDictionaryScrapped(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        DictionaryScrapResponse dictionaryScrapDtos = scrapService.getDictionaryScrapped(user);
        return ResponseEntity.ok(dictionaryScrapDtos);
    }

    /**
     * 스크랩된 용어 삭제 API
     */
    @Operation(summary = "스크랩된 용어 삭제", description = "사용자가 스크랩한 용어를 삭제합니다.")
    @DeleteMapping("/dictionaries/{dictionaryScrapId}")
    public ResponseEntity<String> deleteDictionaryScrapped(
        @PathVariable("dictionaryScrapId") Long dictionaryScrapId,
        @AuthenticationPrincipal CustomUserDetails userDetails){
        Users user = userDetails.getUser();
        scrapService.deleteDictionaryScrapped(dictionaryScrapId, user);
        return ResponseEntity.ok("삭제 성공");
    }

    /**
     * 전체 스크랩 수 조회 API
     */
    @Operation(summary = "전체 스크랩 수 조회", description = "사용자가 스크랩한 전체 콘텐츠 및 용어 수를 조회합니다.")
    @GetMapping("/count")
    public ResponseEntity<TotalScarpCntDto> getTotalScrapCnt(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        TotalScarpCntDto totalScarpCntDto = scrapService.getTotalScarpCnt(user);
        return ResponseEntity.ok(totalScarpCntDto);
    }

    /**
     * 콘텐츠 스크랩 수 조회 API
     */
    @Operation(summary = "콘텐츠 스크랩 수 조회", description = "사용자가 스크랩한 콘텐츠의 개수를 조회합니다.")
    @GetMapping("/contents/count")
    public ResponseEntity<ContentScrapCntDto> getContentScrapCnt(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        ContentScrapCntDto contentScrapCntDto = scrapService.getContentScrapCnt(user);
        return ResponseEntity.ok(contentScrapCntDto);
    }

    /**
     * 콘텐츠 스크랩 수 조회 API
     */
    @Operation(summary = "용어 스크랩 수 조회", description = "사용자가 스크랩한 용어의 개수를 조회합니다.")
    @GetMapping("/dictionaries/count")
    public ResponseEntity<DictionaryScrapCntDto> getDictionaryScrapCnt(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        DictionaryScrapCntDto dictionaryScrapCntDto = scrapService.getDictionaryScrapCnt(user);
        return ResponseEntity.ok(dictionaryScrapCntDto);
    }

}
