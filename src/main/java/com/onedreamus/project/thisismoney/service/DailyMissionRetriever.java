package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.DailyMissionDetail;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusDto;
import com.onedreamus.project.thisismoney.model.dto.MissionStatusResponse;
import com.onedreamus.project.thisismoney.model.entity.Mission;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.MissionRepository;
import com.onedreamus.project.thisismoney.service.impl.MissionRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyMissionRetriever implements MissionRetriever {

    private final MissionRepository missionRepository;

    @Override
    public MissionStatusResponse retrieve(Users user, LocalDate date) {
        MissionStatusDto missionStatusDto = missionRepository
                .findByUserAndDate(user, date)
                .map(mission -> MissionStatusDto.from(mission.getNewsLearnStatus(), mission.getQuizSolveStatus()))
                .orElse(MissionStatusDto.from(false, false));

        return new MissionStatusResponse(missionStatusDto);
    }

    @Override
    public MissionStatusResponse retrieve(Users user, YearMonth yearMonth) {
        throw new UnsupportedOperationException("일별 미션 상태 조회 retrieval 은 YearMonth 를 지원한지 않습니다.");

    }
}
