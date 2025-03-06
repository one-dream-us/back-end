package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.service.DictionaryScrapService;
import com.onedreamus.project.thisismoney.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "users", description = "유저 관련 API")
public class UserController {

    private final UserService userService;
    private final DictionaryScrapService dictionaryScrapService;

    /**
     * 토큰으로 유저 데이터를 잘 반환하는지 테스트하기위한 API
     */
    @GetMapping("/info")
    @Operation(
            summary = "유저 데이터 조회",
            description = "유저데이터 조회 API")
    public ResponseEntity<UserInfoDto> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        UserInfoDto userDto = userService.getUserInfo(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/quiz/first-attempt")
    @Operation(summary = "첫 퀴즈 참여인지 확인", description = "유저가 퀴즈에 처음 참여하는지 확인할 수 있습니다.")
    public ResponseEntity<FirstQuizAttemptResponse> checkFirstQuizAttempt(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        FirstQuizAttemptResponse response = userService.checkFirstAttempt(userDetails.getUser());
        return ResponseEntity.ok(response);
    }


    /**
     * 회원가입
     * 소셜로그인만 진행하기로 함.
     * deprecated
     */
    @Deprecated
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto) {
        userService.join(joinDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        userService.logout(response, user);
        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 - soft delete + 소셜 서비스 연결 해제")
    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdrawMembership(
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users user = userDetails.getUser();
        userService.withdraw(response, user);

//        response.sendRedirect();
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    @Operation(summary = "소셜로그인 회원가입 요청",
            description = "연령 확인 후 결과 값과 토큰을 보내면 회원가입 처리합니다.")
    @PostMapping("/join/social")
    public ResponseEntity<String> joinWithSocialLogin(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> cookies = userService.joinSocial(request);

        // cookie 발급
        for (String cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie);
        }

        return ResponseEntity.ok("회원가입 처리 완료");
    }

    @Operation(summary = "소셜로그인 unlink 처리",
            description = "유저의 소셜 계정과 서비스 간 연동을 해제합니다.")
    @PostMapping("/unlink/social")
    public ResponseEntity<String> unlinkSocial(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<String> cookies = userService.unlinkSocial(request);

        for (String cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie);
        }

        return ResponseEntity.ok("unlink 완료");
    }
//
//    @GetMapping("/me/content-histories/count")
//    @Operation(summary = "사용자가 확인한 콘텐츠 수 조회",
//            description = "현재 로그인한 사용자가 확인한 콘텐츠 수를 조회합니다.")
//    public ResponseEntity<ContentHistoryCountResponse> getContentHistoryCount(
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Users user = userDetails.getUser();
//        return ResponseEntity.ok(contentHistoryService.getContentHistoryCount(user));
//    }
//
//    @GetMapping("/me/dictionary-scraps/contents/{contentId}")
//    @Operation(summary = "해당 사용자의 용어 스크랩 상태 조회", description = "현재 로그인한 사용자의 콘텐츠 상세페이지의 용어별 스크랩 상태를 조회합니다.")
//    public ResponseEntity<List<DictionaryScrapInfo>> getUserDictionaryScrapStatus(
//            @PathVariable Long contentId,
//            @AuthenticationPrincipal CustomUserDetails userDetails) {
//        // 콘텐츠 조회 이력 저장
//        contentHistoryService.saveHistory(contentId, userDetails.getUser());
//        // 스크랩 상태 조회
//        return ResponseEntity.ok(
//                dictionaryScrapService.getUserDictionaryScrapStatus(contentId, userDetails.getUser())
//        );
//    }

    @GetMapping("/study-days/count")
    @Operation(summary = "누적 학습 일수 조회", description = "누적된 학습 일수를 조회합니다.")
    public ResponseEntity<StudyDaysCountDto> getStudyDaysCount(@AuthenticationPrincipal CustomUserDetails userDetails) {

        StudyDaysCountDto studyDaysCountDto = userService.getStudyDaysCount(userDetails.getUser());
        return ResponseEntity.ok(studyDaysCountDto);
    }
}
