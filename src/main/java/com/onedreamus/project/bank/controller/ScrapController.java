package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.service.ScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/content/{contentId}")
    public ResponseEntity<String> scrapContent(@PathVariable("contentId") Integer contentId) {
        scrapService.scrapContent(contentId);
        return ResponseEntity.ok("스크랩 성공");
    }


}
