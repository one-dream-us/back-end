package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.model.dto.ContentListDto;
import com.onedreamus.project.bank.model.dto.ContentListResponse;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.repository.ContentRepository;
import com.onedreamus.project.bank.repository.ContentTagRepository;
import com.onedreamus.project.bank.repository.ContentViewRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentTagRepository contentTagRepository;
    private final ContentViewRepository contentViewRepository;

    public Optional<Content> getContentById(Integer contentId){
        return contentRepository.findById(contentId);
    }

    public ContentListDto getContentList(PageRequest pageRequest) {
        Page<Content> contentPage = contentRepository.findAll(pageRequest);

        List<ContentListResponse> contents = contentPage.getContent().stream()
            .map(content -> {
                List<String> tags = contentTagRepository.findByContentOrderBySequence(content)
                    .stream()
                    .map(contentTag -> contentTag.getTag().getValue())
                    .collect(Collectors.toList());

                Integer viewCount = contentViewRepository.findTotalViewCountByContentId(content.getId());

                return ContentListResponse.builder()
                    .id(content.getId())
                    .title(content.getTitle())
                    .contentUrl(content.getContentUrl())
                    .thumbnailUrl(content.getThumbnailUrl())
                    .createdAt(content.getCreatedAt())
                    .viewCount(viewCount != null ? viewCount : 0)
                    .tags(tags)
                    .build();
            })
            .collect(Collectors.toList());

        return ContentListDto.builder()
            .contents(contents)
            .totalElements(contentPage.getTotalElements())
            .totalPages(contentPage.getTotalPages())
            .hasNext(contentPage.hasNext())
            .build();
    }
}
