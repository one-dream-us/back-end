package com.onedreamus.project.thisismoney.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FirstQuizAttemptResponse {
    private Boolean isFirstQuizAttempt;
}
