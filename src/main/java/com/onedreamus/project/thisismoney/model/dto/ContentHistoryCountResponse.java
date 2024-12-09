package com.onedreamus.project.thisismoney.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentHistoryCountResponse {
	private Integer userId;
	private Long watchedCount;
}
