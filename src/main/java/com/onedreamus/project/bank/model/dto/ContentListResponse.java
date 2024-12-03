package com.onedreamus.project.bank.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentListResponse {
	private Long id;
	private String title;
	private String contentUrl;
	private String thumbnailUrl;
	private LocalDateTime createdAt;
	private int viewCount;
	private List<String> tags;
	private String summaryText;
}