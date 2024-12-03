package com.onedreamus.project.bank.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentListDto {
	private List<ContentListResponse> contents;
	private long totalElements;
	private int totalPages;
	private boolean hasNext;
}