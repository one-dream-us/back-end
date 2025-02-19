package com.onedreamus.project.thisismoney.service.impl;

import com.onedreamus.project.thisismoney.model.dto.MissionStatusResponse;
import com.onedreamus.project.thisismoney.model.entity.Users;

import java.time.LocalDate;
import java.time.YearMonth;

public interface MissionRetriever {
    MissionStatusResponse retrieve(Users user, LocalDate date);

    MissionStatusResponse retrieve(Users user, YearMonth yearMonth);
}
