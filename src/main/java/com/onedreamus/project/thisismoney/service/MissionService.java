package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.thisismoney.exception.MissionException;
import com.onedreamus.project.thisismoney.model.dto.ContinuousDaysResponse;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusResponse;
import com.onedreamus.project.thisismoney.model.dto.MonthlyMissionStatus;
import com.onedreamus.project.thisismoney.model.entity.Mission;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DailyMissionRetriever dailyMissionRetriever;
    private final MonthlyMissionRetriever monthlyMissionRetriever;

    /**
     * <p>뉴스 학습 상태 업데이트</p>
     * 뉴스 학습 상태를 true로 수정한다.
     *
     * @param user
     */
    @Transactional
    public void updateNewsLearnStatus(Users user) {
        Mission mission = missionRepository.findByUserAndDate(user, LocalDate.now())
                .orElseGet(() -> {
                    int continuousDays = getContinuousDays(user, LocalDate.now()).getContinuousDays();
                    return Mission.from(user, continuousDays);
                });

        // 이미 뉴스 학습을 한 경우
        if (mission.getNewsLearnStatus()) {
            return;
        }

        mission.setNewsLearnStatus(true);
        missionRepository.save(mission);
    }

    /**
     * <p>퀴즈 풀이 상태 업데이트</p>
     * 퀴즈 풀이 상태를 true로 수정한다.
     *
     * @param user
     */
    @Transactional
    public void updateQuizSolveStatus(Users user) {
        Mission mission = missionRepository.findByUserAndDate(user, LocalDate.now())
                .orElseGet(() -> {
                    int continuousDays = getContinuousDays(user, LocalDate.now()).getContinuousDays();
                    return Mission.from(user, continuousDays);
                });

        // 이미 퀴즈 풀이를 한 경우
        if (mission.getQuizSolveStatus()) {
            return;
        }

        mission.setQuizSolveStatus(true);
        missionRepository.save(mission);
    }

    @Transactional
    public ContinuousDaysResponse getContinuousDays(Users user, LocalDate now) {
        Optional<Mission> previousMission = missionRepository.findByUserAndDate(user, now.minusDays(1));
        int continuousDays = previousMission
                .map(mission -> mission.getContinuousDays() + 1)
                .orElse(1);
        return new ContinuousDaysResponse(continuousDays);
    }

    public MissionStatusResponse getMissionStatus(Users user, LocalDate date, YearMonth month) {
        if (date != null) {
            return dailyMissionRetriever.retrieve(user, date);
        }
        if (month != null) {
            return monthlyMissionRetriever.retrieve(user, month);
        }

        throw new MissionException(ErrorCode.MISSING_DATE_OR_MONTH_PARAM);
    }


}

