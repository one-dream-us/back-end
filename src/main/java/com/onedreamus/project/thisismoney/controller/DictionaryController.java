package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.DictionaryResponse;
import com.onedreamus.project.thisismoney.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/{keyword}")
    @Operation(summary = "용어 조회", description = "keyword를 포함하는 모든 용어를 조회합니다.")
    public ResponseEntity<List<DictionaryResponse>> searchDictionary(
        @PathVariable("keyword") String keyword) {
        List<DictionaryResponse> dictionaryResponses = dictionaryService.searchDictionary(keyword);
        return ResponseEntity.ok(dictionaryResponses);
    }
}
