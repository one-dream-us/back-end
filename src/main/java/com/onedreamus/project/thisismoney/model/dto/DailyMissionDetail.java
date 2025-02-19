package com.onedreamus.project.thisismoney.model.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DailyMissionDetail {

    private LocalDate date;
    private MissionStatusDto missionStatus;

    public static DailyMissionDetail from(LocalDate date, MissionStatusDto missionStatusDto) {
        return DailyMissionDetail.builder()
                .date(date)
                .missionStatus(missionStatusDto)
                .build();
    }

}
