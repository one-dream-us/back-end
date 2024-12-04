package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.ContentScrapDto;
import com.onedreamus.project.bank.model.dto.DictionaryScrapDto;
import com.onedreamus.project.bank.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
@Tag(name = "Scrap Controller", description = "스크랩 관련 API")
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 콘텐츠 스크랩 API
     */
    @Operation(summary = "콘텐츠 스크랩 추가", description = "콘텐츠 스크랩 추가 API")
    @PostMapping("/content/{contentId}")
    public ResponseEntity<String> scrapContent(@PathVariable("contentId") Integer contentId) {
        scrapService.scrapContent(contentId);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     *스크랩된 콘텐츠 전체 조회 API
     */
    @Operation(summary = "콘텐츠 스크랩 조회", description = "스크랩한 콘텐츠 전체 조회하는 API")
    @GetMapping("/content")
    public ResponseEntity<List<ContentScrapDto>> getContentScrapped() {
        List<ContentScrapDto> contentScrapDtos = scrapService.getContentScrapped();
        return ResponseEntity.ok(contentScrapDtos);
    }

    /**
     * 스크랩된 콘텐츠 삭제 API
     */
    @Operation(summary = "콘텐츠 스크랩 삭제", description = "스크랩한 콘텐츠 삭제하는 API")
    @DeleteMapping("/content/{contentScrapId}")
    public ResponseEntity<String> deleteContentScrapped(@PathVariable("contentScrapId") Integer contentScrapId){
        scrapService.deleteContentScrapped(contentScrapId);
        return ResponseEntity.ok("스크랩 삭제 성공");
    }

    /**
     * 용어 스크랩 API
     */
    @Operation(summary = "용어 스크랩 추가", description = "용어 스크랩 API")
    @PostMapping("/dictionary/{dictionaryId}")
    public ResponseEntity<String> scrapTerm(@PathVariable("dictionaryId") Long dictionaryId) {
        scrapService.scrapDictionary(dictionaryId);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     * 스크랩된 용어 전체 조회 API
     */
    @Operation(summary = "용어 스크랩 조회", description = "스크랩한 용어 전체 조회하는 API")
    @GetMapping("/dictionary")
    public ResponseEntity<List<DictionaryScrapDto>> getDictionaryScrapped(){
        List<DictionaryScrapDto> dictionaryScrapDtos = scrapService.getDictionaryScrapped();
        return ResponseEntity.ok(dictionaryScrapDtos);
    }

    /**
     * 스크랩된 용어 삭제 API
     */
    @Operation(summary = "콘텐츠 용어 삭제", description = "스크랩한 용어 삭제 API")
    @DeleteMapping("/dictionary/{dictionaryScrapId}")
    public ResponseEntity<String> deleteDictionaryScrapped(@PathVariable("dictionaryScrapId") Long dictionaryScrapId){
        scrapService.deleteDictionaryScrapped(dictionaryScrapId);
        return ResponseEntity.ok("삭제 성공");
    }

}
