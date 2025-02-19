package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.DailyMissionDetail;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusDto;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusResponse;
import com.onedreamus.project.thisismoney.model.dto.MonthlyMissionStatus;
import com.onedreamus.project.thisismoney.model.entity.Mission;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.MissionRepository;
import com.onedreamus.project.thisismoney.service.impl.MissionRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlyMissionRetriever implements MissionRetriever {

    private final MissionRepository missionRepository;

    @Override
    public MissionStatusResponse retrieve(Users user, LocalDate date) {
        throw new UnsupportedOperationException("월별 미션 상태 조회 retrieval은 LocalDate를 지원한지 않습니다.");
    }

    @Override
    public MissionStatusResponse retrieve(Users user, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        Map<LocalDate, Mission> missionMap = missionRepository.findByUserAndDateBetween(user, startDate, endDate).stream()
                .collect(Collectors.toMap(Mission::getDate, Function.identity()));

        List<DailyMissionDetail> dailyMissionDetails = new ArrayList<>();
        for (int day = 0; day < endDate.getDayOfMonth(); day++) {
            LocalDate date = startDate.plusDays(day);
            MissionStatusDto missionStatusDto = MissionStatusDto.from(false, false);

            Mission mission = missionMap.get(date);
            if (mission != null) {
                missionStatusDto.setQuiz(mission.getQuizSolveStatus());
                missionStatusDto.setNews(mission.getNewsLearnStatus());
            }

            dailyMissionDetails.add(DailyMissionDetail.from(date, missionStatusDto));
        }

        return new MissionStatusResponse(MonthlyMissionStatus.from(dailyMissionDetails));
    }
}
