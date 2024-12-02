package com.onedreamus.project.bank.controller;

import com.onedreamus.project.bank.model.dto.ContentListDto;
import com.onedreamus.project.bank.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
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
	public ResponseEntity<ContentListDto> getContentList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
		return ResponseEntity.ok(contentService.getContentList(pageRequest));

	}

}
