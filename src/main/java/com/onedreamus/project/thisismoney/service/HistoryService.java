package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.exception.DictionaryException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.DictionaryHistory;
import com.onedreamus.project.thisismoney.model.entity.Users;
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
    private final DictionaryService dictionaryService;

    /**
     * 뉴스 용어 히스토리 추가
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
     * <p>여러 용어 히스토리에 추가</p>
     * - dictionaryIds 값들을 통해 히스토리에 추가
     *
     * @param dictionaryIds
     * @param user
     */
    @Transactional
    public void addHistoryList(List<Long> dictionaryIds, Users user) {
        // 이미 존재하는 history 의 dictionaryId 조회
        List<Long> existingDictionaryIds = dictionaryHistoryRepository.findDictionaryIdsByUserAndDictionaryIds(user, dictionaryIds);

        // 존재하지 않는 dictionaryId 만 필터링
        List<Long> newDictionaryIds = dictionaryIds.stream()
            .filter(id -> !existingDictionaryIds.contains(id))
            .toList();

        if (newDictionaryIds.isEmpty()) {
            return; // 추가할 히스토리가 없으면 종료
        }

        // 새롭게 추가 할 히스토리의 용어 전체 조회
        List<Dictionary> dictionaries = dictionaryService.getAllDictionariesById(newDictionaryIds);

        List<DictionaryHistory> newHistoryList = dictionaries.stream()
            .map(dictionary -> DictionaryHistory.make(user, dictionary))
            .toList();

        dictionaryHistoryRepository.saveAll(newHistoryList);
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

    /**
     * <p>북마크 상태 값 수정</p>
     * false -> true
     * true -> false
     * @param dictionary
     * @param user
     */
    @Transactional
    public void changeBookmarkStatus(Dictionary dictionary, Users user, boolean newBookmarkStatus) {
        dictionaryHistoryRepository.findByUserAndDictionaryAndIsDeleted(user, dictionary, false)
            .ifPresentOrElse(
                // 히스토리가 존재하는 경우
                history -> history.setIsBookmarked(newBookmarkStatus),

                // 히스토리가 존재하지 않는 경우
                () -> {
                    DictionaryHistory history = DictionaryHistory.make(user, dictionary);
                    history.setIsBookmarked(newBookmarkStatus);
                    dictionaryHistoryRepository.save(history);
                }
            );
    }

}
