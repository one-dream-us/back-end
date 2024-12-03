package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.model.dto.ContentDetailResponse;
import com.onedreamus.project.bank.model.dto.ContentListResponse;
import com.onedreamus.project.bank.model.dto.CursorResult;
import com.onedreamus.project.bank.model.dto.ScriptParagraphDto;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ScriptSummary;
import com.onedreamus.project.bank.repository.ContentRepository;
import com.onedreamus.project.bank.repository.ContentScrapRepository;
import com.onedreamus.project.bank.repository.ContentTagRepository;
import com.onedreamus.project.bank.repository.ContentViewRepository;
import com.onedreamus.project.bank.repository.ScriptParagraphRepository;
import com.onedreamus.project.bank.repository.ScriptSummaryRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.NumberFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;
    private  final ContentScrapRepository contentScrapRepository;
    private final ContentTagRepository contentTagRepository;
    private final ContentViewRepository contentViewRepository;
    private final ScriptSummaryRepository scriptSummaryRepository;
    private final ScriptParagraphRepository scriptParagraphRepository;

    public Optional<Content> getContentById(Integer contentId){
        return contentRepository.findById(contentId);
    }

    public CursorResult<ContentListResponse> getContentList(Long cursor, int size) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);
        List<Content> contents = contentRepository.findByIdLessThanOrderByIdDesc(
            cursor, pageRequest);
        long totalElements = contentRepository.countTotalContents();

        boolean hasNext = contents.size() > size;
        if (hasNext) {
            contents.remove(contents.size() - 1);
        }

        List<ContentListResponse> responses = contents.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());


        Long nextCursor = hasNext && !responses.isEmpty() ?
            responses.get(responses.size() - 1).getId() : null;

        return CursorResult.<ContentListResponse>builder()
            .contents(responses)
            .hasNext(hasNext)
            .nextCursor(nextCursor)
            .totalElements(totalElements)
            .build();
    }

    public ContentListResponse getLatestContent() {
        Content content = contentRepository.findTopByOrderByCreatedAtDesc()
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));
        return convertToResponse(content);
    }

    public List<ContentListResponse> getPopularContents() {
        List<Content> contents = contentRepository.findTop5ByOrderByViewCountDesc();
        return contents.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    private ContentListResponse convertToResponse(Content content) {
        List<String> tags = contentTagRepository.findByContentOrderBySequence(content)
            .stream()
            .map(contentTag -> contentTag.getTag().getValue())
            .collect(Collectors.toList());

        Integer viewCount = contentViewRepository.findTotalViewCountByContentId(content.getId());
        String formattedViewCount = NumberFormatter.format(viewCount != null ? viewCount : 0);
        Integer scrapCount = contentScrapRepository.countByContentId(content.getId());
        String formattedScrapCount = NumberFormatter.format(scrapCount != null ? scrapCount : 0);

        String summaryText = scriptSummaryRepository
            .findByContentId(content.getId())
            .map(ScriptSummary::getSummaryText)
            .orElse(null);

        return ContentListResponse.builder()
            .id(content.getId())
            .title(content.getTitle())
            .contentUrl(content.getContentUrl())
            .thumbnailUrl(content.getThumbnailUrl())
            .createdAt(content.getCreatedAt())
            .viewCount(formattedViewCount)
            .scrapCount(formattedScrapCount)
            .tags(tags)
            .summaryText(summaryText)
            .build();
    }

    public ContentDetailResponse getContentDetail(Long contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));

        List<String> tags = contentTagRepository.findByContentOrderBySequence(content)
            .stream()
            .map(contentTag -> contentTag.getTag().getValue())
            .collect(Collectors.toList());

        Integer viewCount = contentViewRepository.findTotalViewCountByContentId(content.getId());
        String formattedViewCount = NumberFormatter.format(viewCount != null ? viewCount : 0);
        Integer scrapCount = contentScrapRepository.countByContentId(content.getId());
        String formattedScrapCount = NumberFormatter.format(scrapCount != null ? scrapCount : 0);

        String summaryText = scriptSummaryRepository
            .findByContentId(content.getId())
            .map(ScriptSummary::getSummaryText)
            .orElse(null);

        List<ScriptParagraphDto> scriptParagraphs = scriptParagraphRepository
            .findByContentIdOrderByTimestamp(content.getId())
            .stream()
            .map(sp -> ScriptParagraphDto.builder()
                .timestamp(sp.getTimestamp())
                .paragraphText(sp.getParagraphText())
                .build())
            .collect(Collectors.toList());

        return ContentDetailResponse.builder()
            .id(content.getId())
            .title(content.getTitle())
            .contentUrl(content.getContentUrl())
            .thumbnailUrl(content.getThumbnailUrl())
            .createdAt(content.getCreatedAt())
            .viewCount(formattedViewCount)
            .scrapCount(formattedScrapCount)
            .tags(tags)
            .summaryText(summaryText)
            .author(content.getAuthor())
            .scriptParagraphs(scriptParagraphs)
            .build();
    }
}
