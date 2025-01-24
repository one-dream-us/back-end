package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudyDaysCountDto {

    private int studyDaysCnt;

    public static StudyDaysCountDto from(int totalStudyDays) {

        return StudyDaysCountDto.builder()
                .studyDaysCnt(totalStudyDays)
                .build();
    }
}
