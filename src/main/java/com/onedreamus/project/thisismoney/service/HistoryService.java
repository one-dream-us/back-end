package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.exception.ScrapException;
import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryHistory;
import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.repository.DictionaryScrapContentRepository;
import com.onedreamus.project.thisismoney.repository.DictionaryHistoryRepository;
import com.onedreamus.project.global.exception.ErrorCode;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

    private final DictionaryHistoryRepository dictionaryHistoryRepository;
//    private final ContentScrapRepository contentScrapRepository;
//    private final ContentService contentService;
    private final DictionaryService dictionaryService;
    private final DictionaryScrapContentRepository dictionaryScrapContentRepository;
//
//    /**
//     * 콘텐츠 스크랩
//     */
//    public void scrapContent(Long contentId, Users user) {
//        // 컨텐츠가 없는 경우
//        Content content = contentService.getContentById(contentId)
//                .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));
//
//        // 기존에 스크랩한 항목인지 점검
//        Optional<ContentScrap> contentScrapOptional =
//                contentScrapRepository.findByUserAndContentAndIsDeleted(user, content, false);
//
//        // 이전에 스크랩한적 없는 경우
//        if (contentScrapOptional.isEmpty()) {
//            ContentScrap newContentScrap = ContentScrap.from(user, content);
//            contentScrapRepository.save(newContentScrap);
//        } else {
//            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
//        }
//    }
//
//    /**
//     * 스크랩된 콘텐츠 전체 획득
//     */
//    public ContentScrapResponse getContentScrapped(Users user) {
//
//        Map<Long, ContentScrapDto> map = new HashMap<>();
//
//        List<ContentScrapSummaryDto> contentScrapSummaryDtos =
//                contentScrapRepository.findContentScrapSummaryByUser(user);
//        for (ContentScrapSummaryDto dto : contentScrapSummaryDtos) {
//
//            map.put(dto.getScrapId(), ContentScrapDto.builder()
//                    .scrapId(dto.getScrapId())
//                    .contentId(dto.getContentId())
//                    .thumbnailUrl(dto.getThumbnailUrl())
//                    .contentTitle(dto.getContentTitle())
//                    .summaryText(dto.getSummaryText())
//                    .createdAt(dto.getCreatedAt())
//                    .tags(new ArrayList<>())
//                    .build());
//        }
//
//        List<ContentScrapTagDto> contentScrapTagDtos =
//                contentScrapRepository.findContentScrapTagByUser(user);
//        for (ContentScrapTagDto dto : contentScrapTagDtos) {
//            ContentScrapDto contentScrapDto = map.get(dto.getScrapId());
//            contentScrapDto.getTags().add(TagDto.builder()
//                    .tagValue(dto.getTagValue())
//                    .build());
//        }
//
//        List<ContentScrapDto> result = new ArrayList<>(map.values());
//        result.sort(Comparator.comparing(ContentScrapDto::getCreatedAt).reversed());
//
//        return ContentScrapResponse.from(result);
//    }
//
//    /**
//     * 스크랩된 콘텐츠 삭제
//     */
//    public void deleteContentScrapped(Integer contentScrapId, Users user) {
//        ContentScrap contentScrap = contentScrapRepository.findByIdAndUser(contentScrapId, user)
//                .orElseThrow(() -> new ScrapException(ErrorCode.SCRAP_NOT_EXIST));
//
//        contentScrap.setIsDeleted(true);
//        contentScrapRepository.save(contentScrap);
//    }
//
//    /**
//     * 용어 스크랩
//     */
//    public void scrapDictionary(Long dictionaryId, Long contentId, Users user) {
//        // 스크랩하려는 용어가 존재하는 용어인지 확인
//        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
//                .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));
//
//        // 콘텐츠 확인
//        Content content = contentService.getContentById(contentId)
//                .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));
//
//        // 기존에 스크랩된 용어인지 확인
//        Optional<DictionaryHistory> dictionaryScrapOptional =
//                dictionaryHistoryRepository.findByUserAndDictionaryAndIsDeleted(user, dictionary, false);
//
//        if (dictionaryScrapOptional.isEmpty()) { // 스크랩된 적 없는 경우
//
//            DictionaryHistory newDictionaryHistory =
//                    dictionaryHistoryRepository.save(DictionaryHistory.make(user, dictionary));
//
//            dictionaryScrapContentRepository.save(
//                    DictionaryScrapContent.from(newDictionaryHistory, content));
//        } else { // 이미 스크랩된 경우
//            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
//        }
//    }

    /**
     * 뉴스 용어 스크랩
     */
    @Transactional
    public void addHistory(Long dictionaryId, Users user) {
        // 히스토리에 추가하려는 용어가 존재하는 용어인지 확인
        dictionaryService.getDictionaryById(dictionaryId)
            .ifPresentOrElse(dictionary -> {
                // 기존에 히스토리에 존재하는 용어인지 확인
                boolean isHistoryExists = dictionaryHistoryRepository.existsByUserAndDictionaryAndIsDeleted(user, dictionary, false);
                if (!isHistoryExists) { // 히스토리에 없는 경우
                    dictionaryHistoryRepository.save(DictionaryHistory.make(user, dictionary));
                }
            }, () -> {// 존재하지 않는 용어일 경우 예외 발생
                throw new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST);
            });
    }

    /**
     * <p>스크랩 추가</p>
     * dictionaryId 로만 스크랩 추가
     */
    public void add(Dictionary dictionary, Users user) {
        dictionaryHistoryRepository.save(DictionaryHistory.make(user, dictionary));
    }

    /**
     * 히스토리에 있는 용어 전체 조회
     */
    public DictionaryHistoryResponse getHistoryList(Users user) {

        List<DictionaryNewsDto> dictionaryHistoryList =
                dictionaryHistoryRepository.findDictionaryHistoryByUser(user);

        return DictionaryHistoryResponse.from(dictionaryHistoryList);
    }

    /**
     * 히스토리 관련 데이터 삭제
     */
    @Transactional
    public void deleteAllHistory(Users user) {
        // 기존에 스크랩된 모든 용어 삭제
        List<DictionaryHistory> allDictionaryHistories = dictionaryHistoryRepository.findAllByUser(user);
        for (DictionaryHistory dictionaryHistory : allDictionaryHistories) {
            dictionaryHistory.setIsDeleted(true);
        }
        
        dictionaryHistoryRepository.saveAll(allDictionaryHistories);
    }

    /**
     * @param user user가 스크랩한 용어 리스트 조회
     */
    public List<DictionaryHistory> getDictionaryHistoryList(Users user) {
        return dictionaryHistoryRepository.findByUserAndIsDeletedFalse(user);
    }

    /**
     * 히스토리 아이디 리스트 획득
     * @param user
     * @return history ID 리스트
     */
    public List<Long> getDictionaryHistoryIds(Users user) {
        return dictionaryHistoryRepository.findIdByUser(user);
    }


//
//    /**
//     * 전체 스크랩 수 조회
//     */
//    public TotalScarpCntDto getTotalScarpCnt(Users user) {
//
//        int totalScrapCnt = 0;
//        totalScrapCnt += getContentScrapCnt(user).getContentScrapCnt();
//        totalScrapCnt += getDictionaryScrapCnt(user).getDictionaryScrapCnt();
//
//        return TotalScarpCntDto.builder()
//                .totalScrapCnt(totalScrapCnt)
//                .build();
//    }

//    /**
//     * 콘텐츠 스크랩 수 조회
//     */
//    public ContentScrapCntDto getContentScrapCnt(Users user) {
//        Integer contentScrapCnt = contentScrapRepository.countByUserAndIsDeleted(user, false);
//        return ContentScrapCntDto.builder()
//                .contentScrapCnt(contentScrapCnt)
//                .build();
//    }

}
