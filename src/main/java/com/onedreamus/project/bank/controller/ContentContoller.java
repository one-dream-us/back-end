package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.ContentListDto;
import com.onedreamus.project.bank.model.dto.ContentListResponse;
import com.onedreamus.project.bank.model.dto.CursorResult;
import com.onedreamus.project.bank.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/content")
@RequiredArgsConstructor
@Tag(name = "콘텐츠", description = "콘텐츠 관련 API")
public class ContentContoller {

	private final ContentService contentService;

	@GetMapping
	@Operation(summary = "콘텐츠 리스트 조회", description = "콘텐츠 리스트를 조회합니다.")
	public ResponseEntity<CursorResult<ContentListResponse>> getContentList(
		@Parameter(description = "마지막으로 조회된 콘텐츠의 ID (첫 페이지 조회 시 null)",
			example = "123")
		@RequestParam(required = false) Long cursor,
		@Parameter(description = "한 페이지당 조회할 콘텐츠 수",
			example = "10",
			schema = @Schema(minimum = "1", maximum = "100"))
		@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(contentService.getContentList(cursor, size));
	}

}
