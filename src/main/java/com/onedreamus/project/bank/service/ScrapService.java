package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.exception.ScrapException;
import com.onedreamus.project.bank.exception.DictionaryException;
import com.onedreamus.project.bank.model.dto.ContentScrapDto;
import com.onedreamus.project.bank.model.dto.*;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.Dictionary;
import com.onedreamus.project.bank.model.entity.DictionaryScrap;
import com.onedreamus.project.bank.model.entity.DictionaryScrapContent;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.ContentScrapRepository;
import com.onedreamus.project.bank.repository.DictionaryScrapContentRepository;
import com.onedreamus.project.bank.repository.DictionaryScrapRepository;
import com.onedreamus.project.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.PriorityQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapService {

    private final DictionaryScrapRepository dictionaryScrapRepository;
    private final ContentScrapRepository contentScrapRepository;
    private final ContentService contentService;
    private final DictionaryService dictionaryService;
    private final DictionaryScrapContentRepository dictionaryScrapContentRepository;
    private final UserService userService;

    /**
     * 콘텐츠 스크랩
     */
    public void scrapContent(Long contentId, Users user) {
        // 컨텐츠가 없는 경우
        Content content = contentService.getContentById(contentId)
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));

        // 기존에 스크랩한 항목인지 점검
        Optional<ContentScrap> contentScrapOptional =
            contentScrapRepository.findByUserAndContentAndIsDeleted(user, content, false);

        // 이전에 스크랩한적 없는 경우
        if (contentScrapOptional.isEmpty()) {
            ContentScrap newContentScrap = ContentScrap.from(user, content);
            contentScrapRepository.save(newContentScrap);
        } else {
            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
        }
    }

    /**
     * 스크랩된 콘텐츠 전체 획득
     */
    public ContentScrapResponse getContentScrapped(Users user) {

        Map<Long, ContentScrapDto> map = new HashMap<>();

        List<ContentScrapSummaryDto> contentScrapSummaryDtos =
            contentScrapRepository.findContentScrapSummaryByUser(user);
        for (ContentScrapSummaryDto dto : contentScrapSummaryDtos) {

            map.put(dto.getScrapId(), ContentScrapDto.builder()
                .scrapId(dto.getScrapId())
                .contentId(dto.getContentId())
                .thumbnailUrl(dto.getThumbnailUrl())
                .contentTitle(dto.getContentTitle())
                .summaryText(dto.getSummaryText())
                .createdAt(dto.getCreatedAt())
                .tags(new ArrayList<>())
                .build());
        }

        List<ContentScrapTagDto> contentScrapTagDtos =
            contentScrapRepository.findContentScrapTagByUser(user);
        for (ContentScrapTagDto dto : contentScrapTagDtos) {
            ContentScrapDto contentScrapDto = map.get(dto.getScrapId());
            contentScrapDto.getTags().add(TagDto.builder()
                .tagValue(dto.getTagValue())
                .sequence(dto.getSequence())
                .build());
        }

        List<ContentScrapDto> result = new ArrayList<>(map.values());
        Collections.sort(result, Comparator.comparing(ContentScrapDto::getCreatedAt));

        return ContentScrapResponse.from(result);
    }

    /**
     * 스크랩된 콘텐츠 삭제
     */
    public void deleteContentScrapped(Integer contentScrapId, Users user) {
        ContentScrap contentScrap = contentScrapRepository.findByIdAndUser(contentScrapId, user)
            .orElseThrow(() -> new ScrapException(ErrorCode.SCRAP_NO_EXIST));

        contentScrap.setIsDeleted(true);
        contentScrapRepository.save(contentScrap);
    }

    /**
     * 용어 스크랩
     */
    public void scrapDictionary(Long dictionaryId, Long contentId, Users user) {
        // 스크랩하려는 용어가 존재하는 용어인지 확인
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
            .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));

        // 콘텐츠 확인
        Content content = contentService.getContentById(contentId)
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));

        // 기존에 스크랩된 용어인지 확인
        Optional<DictionaryScrap> DictionaryScrapOptional =
                dictionaryScrapRepository.findByUserAndDictionaryAndIsDeleted(user, dictionary, false);

        if (DictionaryScrapOptional.isEmpty()) { // 스크랩된 적 없는 경우

            DictionaryScrap newDictionaryScrap =
                dictionaryScrapRepository.save(DictionaryScrap.make(user, dictionary));

            dictionaryScrapContentRepository.save(
                DictionaryScrapContent.from(newDictionaryScrap, content));
        } else { // 이미 스크랩된 경우
            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
        }
    }

    /**
     * 스크랩된 용어 전체 조회
     */
    public DictionaryScrapResponse getDictionaryScrapped(Users user) {

        List<DictionaryContentDto> dictionaryScrapContents =
            dictionaryScrapRepository.findDictionaryScrapWithContentByUser(user);

        return DictionaryScrapResponse.from(dictionaryScrapContents);
    }

    /**
     * 스크랩된 용어 삭제
     */
    public void deleteDictionaryScrapped(Long dictionaryScrapId, Users user) {
        DictionaryScrap dictionaryScrap = dictionaryScrapRepository.findByIdAndUser(dictionaryScrapId, user)
            .orElseThrow(() -> new ScrapException(ErrorCode.SCRAP_NO_EXIST));

        dictionaryScrap.setIsDeleted(true);

        dictionaryScrapRepository.save(dictionaryScrap);
    }

    /**
     * 전체 스크랩 수 조회
     */
    public TotalScarpCntDto getTotalScarpCnt(Users user) {

        int totalScrapCnt = 0;
        totalScrapCnt += getContentScrapCnt(user).getContentScrapCnt();
        totalScrapCnt += getDictionaryScrapCnt(user).getDictionaryScrapCnt();

        return TotalScarpCntDto.builder()
            .totalScrapCnt(totalScrapCnt)
            .build();
    }

    /**
     * 콘텐츠 스크랩 수 조회
     */
    public ContentScrapCntDto getContentScrapCnt(Users user) {
        Integer contentScrapCnt = contentScrapRepository.countByUser(user);
        return ContentScrapCntDto.builder()
            .contentScrapCnt(contentScrapCnt)
            .build();
    }

    /**
     * 용어 스크랩 수 조회
     */
    public DictionaryScrapCntDto getDictionaryScrapCnt(Users user) {
        Integer dictionaryScrapCnt = dictionaryScrapRepository.countByUserAndIsDeleted(user, false);
        return DictionaryScrapCntDto.builder()
            .dictionaryScrapCnt(dictionaryScrapCnt)
            .build();
    }
}
