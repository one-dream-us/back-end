package com.onedreamus.project.bank.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DictionaryScrapInfo {
	private Long dictionaryId;
	private Long dictionaryScrapId;
	private boolean isScrapped;
}
