package com.onedreamus.project.thisismoney.controller;

import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.service.ContentService;
import com.onedreamus.project.thisismoney.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contents")
@RequiredArgsConstructor
@Tag(name = "contents", description = "콘텐츠 관련 API")
public class ContentController {

	private final ContentService contentService;
	private final NewsService newsService;

	@GetMapping
	@Operation(summary = "콘텐츠 목록 조회", description = "여러 개의 콘텐츠 목록을 가져옵니다. 최신 콘텐츠부터 조회할 수 있습니다. 'cursor'를 사용해 마지막으로 본 콘텐츠 이후부터 조회할 수 있습니다.")
	public ResponseEntity<CursorResult<ContentListResponse>> getContentList(
		@Parameter(description = "마지막으로 조회된 콘텐츠의 ID (첫 페이지 조회 시 null)",
			example = "123")
		@RequestParam(required = false) Long cursor,
		@Parameter(description = "한 번에 가져올 콘텐츠의 개수",
			example = "10",
			schema = @Schema(minimum = "1", maximum = "100"))
		@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(contentService.getContentList(cursor, size));
	}

	@GetMapping("/latest")
	@Operation(summary = "가장 최근 콘텐츠 조회", description = "가장 최근에 업로드된 콘텐츠 1개를 가져옵니다.")
	public ResponseEntity<ContentListResponse> getLatestContent() {
		return ResponseEntity.ok(contentService.getLatestContent());
	}

	@GetMapping("/popular")
	@Operation(summary = "인기 콘텐츠 조회", description = "가장 많이 조회된 인기 콘텐츠 5개를 가져옵니다.")
	public ResponseEntity<List<ContentListResponse>> getPopularContents() {
		return ResponseEntity.ok(contentService.getPopularContents());
	}

	@GetMapping("/{contentId}")
	@Operation(summary = "콘텐츠 상세 조회", description = "특정 콘텐츠의 상세 정보를 가져옵니다. 콘텐츠 ID를 이용해 해당 콘텐츠의 세부 정보를 확인할 수 있습니다.")
	public ResponseEntity<ContentDetailResponse> getContentDetail(
		@Parameter(description = "조회할 콘텐츠의 ID", example = "1")
		@PathVariable Long contentId) {

		return ResponseEntity.ok(contentService.getContentDetail(contentId));
	}

	@GetMapping("/news")
	@Operation(summary = "뉴스 콘텐츠 목록 조회", description = "여러 개의 뉴스 목록을 가져옵니다. 최신 뉴스부터 조회 할 수 있습니다. 'cursor'를 사용해 마지막으로 본 뉴스 이후부터 조회할 수 있습니다.")
	public ResponseEntity<CursorResult<NewsListResponse>> getNewsList(
		@Parameter(description = "마지막으로 조회된 콘텐츠의 ID (첫 페이지 조회 시 null)",
			example = "123")
		@RequestParam(required = false) Integer cursor,
		@Parameter(description = "한 번에 가져올 콘텐츠의 개수",
			example = "10",
			schema = @Schema(minimum = "1", maximum = "100"))
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(newsService.getNewList(cursor, size));
	}

	@GetMapping("/news/{newsId}")
	@Operation(summary = "뉴스 상세페이지 조회", description = "뉴스 상세페이지로 이동하여 학습을 합니다.")
	public ResponseEntity<NewsDetailResponse> getNewsDetail(@PathVariable Integer newsId) {
		return ResponseEntity.ok(newsService.getNewsDetail(newsId));
	}
}
