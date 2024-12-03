package com.onedreamus.project.bank.model.dto;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScriptParagraphDto {
	private LocalTime timestamp;
	private String paragraphText;
}
