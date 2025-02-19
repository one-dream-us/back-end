package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.ContinuousDaysResponse;
import com.onedreamus.project.thisismoney.model.dto.CustomUserDetails;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusResponse;
import com.onedreamus.project.thisismoney.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/v1/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/status")
    @Operation(summary = "미션 상태 조회", description = "월 단위 또는 일 단위로 미션 상태를 조회합니다.")
    public ResponseEntity<MissionStatusResponse> getMonthlyMissionStatus(
            @RequestParam(required = false, value = "date") LocalDate date,
            @RequestParam(required = false, value = "month") YearMonth month,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MissionStatusResponse missionStatusResponse =
                missionService.getMissionStatus(userDetails.getUser(), date, month);
        return ResponseEntity.ok(missionStatusResponse);
    }

    @GetMapping("/status/continuous-days")
    @Operation(summary = "연속 학습 일수 조회", description = "연속 학습 일수를 조회합니다.")
    public ResponseEntity<ContinuousDaysResponse> getContinuousDays(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ContinuousDaysResponse continuousDaysResponse = missionService.getContinuousDays(userDetails.getUser(), LocalDate.now());
        return ResponseEntity.ok(continuousDaysResponse);
    }

    @PostMapping("/status/news-learn")
    @Operation(summary = "뉴스 학습 상태 수정", description = "뉴스 학습 상태를 true로 변경합니다.")
    public ResponseEntity<String> updateNewsLearnMission(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        missionService.updateNewsLearnStatus(userDetails.getUser());
        return ResponseEntity.ok("학습 상태 수정 성공");
    }
}
