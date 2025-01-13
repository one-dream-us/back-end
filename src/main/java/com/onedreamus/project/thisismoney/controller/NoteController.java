package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/note")
@Slf4j
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "핵심노트 조회", description = "핵심노트를 조회합니다.")
    @GetMapping("/key-note")
    public ResponseEntity<KeyNoteResponse> getKeyNoteList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        KeyNoteResponse keyNoteResponse = noteService.getKeyNoteList(user);
        return ResponseEntity.ok(keyNoteResponse);
    }

    @Operation(summary = "용어 핵심노트에 추가", description = "스크랩된 용어를 핵십노트에 추가합니다. dictionaryId로")
    @PostMapping("/key-note/dictionary/{dictionaryId}")
    public ResponseEntity<String> addKeynote(
            @PathVariable Long dictionaryId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        noteService.addKeyNote(dictionaryId, user);
        return ResponseEntity.ok("핵심 노트에 추가되었습니다.");
    }

    @Operation(summary = "핵심노트 취소", description = "핵심노트 취소하면 용어가 스크랩으로 이동합니다.")
    @DeleteMapping("/key-note/{keyNoteId}")
    public ResponseEntity<String> deleteKeyNote(
            @PathVariable Long keyNoteId, @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Users user = userDetails.getUser();
        noteService.deleteKeyNote(keyNoteId, user);
        return ResponseEntity.ok("핵심 노트에서 삭제되었습니다.");
    }

    @Operation(summary = "오답노트 조회", description = "오답노트를 조회합니다.")
    @GetMapping("/wrong-answer-note")
    public ResponseEntity<WrongAnswerNoteResponse> getWrongAnswerNoteList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        WrongAnswerNoteResponse wrongAnswerNoteResponse = noteService.getWrongAnswerList(user);
        return ResponseEntity.ok(wrongAnswerNoteResponse);
    }

    @Operation(summary = "졸업노트 조회", description = "졸업노트를 조회합니다.")
    @GetMapping("/graduation-note")
    public ResponseEntity<GraduationNoteResponse> getGraduationNoteList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        GraduationNoteResponse graduationNoteResponse = noteService.getGraduationNoteList(user);
        return ResponseEntity.ok(graduationNoteResponse);
    }

    @Operation(summary = "학습상태창 조회", description = "스크랩, 핵심노트, 졸업 용어 현황을 조회합니다.")
    @GetMapping("/learning-status/")
    public ResponseEntity<LearningStatus> getLearningStatus(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        LearningStatus learningStatus = noteService.getLearningStatus(user);
        return ResponseEntity.ok(learningStatus);
    }
}
