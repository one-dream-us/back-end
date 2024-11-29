package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.ContentScrapDto;
import com.onedreamus.project.bank.model.dto.TermScrapDto;
import com.onedreamus.project.bank.service.ScrapService;
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
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 콘텐츠 스크랩 API
     */
    @PostMapping("/content/{contentId}")
    public ResponseEntity<String> scrapContent(@PathVariable("contentId") Integer contentId) {
        scrapService.scrapContent(contentId);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     *스크랩된 콘텐츠 전체 조회 API
     */
    @GetMapping("/content")
    public ResponseEntity<List<ContentScrapDto>> getContentScrapped() {
        List<ContentScrapDto> contentScrapDtos = scrapService.getContentScrapped();
        return ResponseEntity.ok(contentScrapDtos);
    }

    /**
     * 스크랩된 콘텐츠 삭제 API
     */
    @DeleteMapping("/content/{contentScrapId}")
    public ResponseEntity<String> deleteContentScrapped(@PathVariable("contentScrapId") Integer contentScrapId){
        scrapService.deleteContentScrapped(contentScrapId);
        return ResponseEntity.ok("스크랩 삭제 성공");
    }

    /**
     * 용어 스크랩 API
     */
    @PostMapping("/term/{termId}")
    public ResponseEntity<String> scrapTerm(@PathVariable("termId") Integer termId) {
        scrapService.scrapTerm(termId);
        return ResponseEntity.ok("스크랩 성공");
    }

    /**
     * 스크랩된 용어 전체 조회 API
     */
    @GetMapping("/term")
    public ResponseEntity<List<TermScrapDto>> getTermScrapped(){
        List<TermScrapDto> termScrapDtos = scrapService.getTermScrapped();
        return ResponseEntity.ok(termScrapDtos);
    }

    /**
     * 스크랩된 용어 삭제 API
     */
    @DeleteMapping("/term/{termScrapId}")
    public ResponseEntity<String> deleteTermScrapped(@PathVariable("termScrapId") Integer termScrapId){
        scrapService.deleteTermScrapped(termScrapId);
        return ResponseEntity.ok("삭제 성공");
    }

}
