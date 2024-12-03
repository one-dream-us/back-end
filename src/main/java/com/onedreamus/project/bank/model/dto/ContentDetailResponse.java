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
	private int viewCount;
	private List<String> tags;
	private String summaryText;
	private String author;  // 추가
	private List<ScriptParagraphDto> scriptParagraphs;  // 추가
}
