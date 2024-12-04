package com.onedreamus.project.bank.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentDetailResponse {
	private Long id;
	private String title;
	private String contentUrl;
	private String thumbnailUrl;
	private LocalDateTime createdAt;
	private String viewCount;
	private String scrapCount;
	private List<String> tags;
	private String summaryText;
	private String author;
	private List<ScriptParagraphDto> scriptParagraphs;
	private String videoId;
}
