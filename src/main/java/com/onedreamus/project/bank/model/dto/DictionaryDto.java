package com.onedreamus.project.bank.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DictionaryDto {
	private Long id;
	private String term;
	private String details;
	private boolean isScrapped;
	private Long dictionaryScrapId;
}
