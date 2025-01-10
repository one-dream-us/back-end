package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 문제 획득", description = "로그인한 유저에 대한 퀴즈 문제를 획득합니다.")
    @GetMapping
    public ResponseEntity<List<Quiz>> getQuizList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Quiz> quizList = quizService.getQuizList(userDetails.getUser());
        return ResponseEntity.ok(quizList);
    }

    @Operation(summary = "더미 퀴즈 문제 획득", description = "로그인한 유저에 대한 더미 퀴즈 문제를 획득합니다.")
    @GetMapping("/random")
    public ResponseEntity<List<Quiz>> getRandomQuizList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Quiz> quizList = quizService.getRandomQuizList(userDetails.getUser());
        return ResponseEntity.ok(quizList);
    }

    @Operation(summary = "퀴즈 결과 처리", description = "퀴즈 결과를 받아 용어 상태를 처리합니다.")
    @PostMapping
    public ResponseEntity<QuizResultResponse> processResults(
            @RequestBody List<QuizResult> quizResults, @AuthenticationPrincipal CustomUserDetails userDetails) {
        QuizResultResponse response = quizService.processQuizResult(userDetails.getUser(), quizResults);
        return ResponseEntity.ok(response);
    }
}
