package com.onedreamus.project.thisismoney.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CursorResult<T> {
	private List<T> contents;
	private boolean hasNext;
	private Long nextCursor;
	private long totalElements;

}
