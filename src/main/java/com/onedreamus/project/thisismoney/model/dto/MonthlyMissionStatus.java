package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MonthlyMissionStatus {

    private List<DailyMissionDetail> dailyMissionDetails;

    public static MonthlyMissionStatus from(List<DailyMissionDetail> dailyMissionDetails) {
        return MonthlyMissionStatus.builder()
                .dailyMissionDetails(dailyMissionDetails)
                .build();
    }
}
