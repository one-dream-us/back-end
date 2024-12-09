package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.exception.ContentException;
import com.onedreamus.project.thisismoney.model.dto.ContentDetailResponse;
import com.onedreamus.project.thisismoney.model.dto.ContentListResponse;
import com.onedreamus.project.thisismoney.model.dto.CursorResult;
import com.onedreamus.project.thisismoney.model.dto.DictionaryDto;
import com.onedreamus.project.thisismoney.model.dto.ScriptParagraphDto;
import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentView;
import com.onedreamus.project.thisismoney.model.entity.ScriptSummary;
import com.onedreamus.project.thisismoney.repository.ContentRepository;
import com.onedreamus.project.thisismoney.repository.ContentScrapRepository;
import com.onedreamus.project.thisismoney.repository.ContentTagRepository;
import com.onedreamus.project.thisismoney.repository.ContentViewRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryScrapRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphDictionaryRepository;
import com.onedreamus.project.thisismoney.repository.ScriptParagraphRepository;
import com.onedreamus.project.thisismoney.repository.ScriptSummaryRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.NumberFormatter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
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
    private final ScriptParagraphDictionaryRepository scriptParagraphDictionaryRepository;
    private final DictionaryScrapRepository dictionaryScrapRepository;

    public Optional<Content> getContentById(Long contentId){
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
            .videoId(extractVideoId(content.getContentUrl()))
            .build();
    }

    @Transactional(readOnly = false)
    public ContentDetailResponse getContentDetail(Long contentId) {
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));

        incrementViewCount(content);

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

        Set<String> processedTerms = new HashSet<>();

        List<ScriptParagraphDto> scriptParagraphs = scriptParagraphRepository
            .findByContentIdOrderByTimestamp(content.getId())
            .stream()
            .map(sp -> {
                AtomicReference<String> paragraphTextRef = new AtomicReference<>(sp.getParagraphText());

                List<DictionaryDto> dictionaries = scriptParagraphDictionaryRepository
                    .findByScriptParagraphIdWithDictionary(sp.getId())
                    .stream()
                    .filter(mapping -> {
                        String term = mapping.getDictionary().getTerm();
                        boolean isFirstAppearance = processedTerms.add(term);

                        if (!isFirstAppearance) {
                            paragraphTextRef.set(paragraphTextRef.get()
                                .replace("<mark>" + term + "</mark>", term));
                        }
                        return isFirstAppearance;
                    })
                    .map(mapping -> DictionaryDto.builder()
                        .id(mapping.getDictionary().getId())
                        .term(mapping.getDictionary().getTerm())
                        .details(mapping.getDictionary().getDetails())
                        .isScrapped(false)
                        .build())
                    .collect(Collectors.toList());

                return ScriptParagraphDto.builder()
                    .id(sp.getId())
                    .timestamp(sp.getTimestamp())
                    .paragraphText(paragraphTextRef.get())
                    .dictionaries(dictionaries)
                    .build();
            })
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
            .videoId(extractVideoId(content.getContentUrl()))
            .build();
    }

    private String extractVideoId(String contentUrl) {
        if (contentUrl == null || !contentUrl.contains("v=")) {
            return null;
        }
        String[] parts = contentUrl.split("v=");
        if (parts.length < 2) {
            return null;
        }
        String videoId = parts[1];
        int ampersandIndex = videoId.indexOf("&");
        if (ampersandIndex != -1) {
            videoId = videoId.substring(0, ampersandIndex);
        }
        return videoId;
    }

    @Transactional(readOnly = false)
    public void incrementViewCount(Content content) {
        contentViewRepository.upsertViewCount(content.getId());
    }
}
