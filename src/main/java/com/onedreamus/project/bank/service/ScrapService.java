package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.exception.ScrapException;
import com.onedreamus.project.bank.exception.DictionaryException;
import com.onedreamus.project.bank.model.dto.ContentScrapDto;
import com.onedreamus.project.bank.model.dto.DictionaryScrapDto;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.Dictionary;
import com.onedreamus.project.bank.model.entity.DictionaryScrap;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.ContentScrapRepository;
import com.onedreamus.project.bank.repository.DictionaryScrapRepository;
import com.onedreamus.project.global.exception.ErrorCode;

import java.util.List;
import java.util.Optional;

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
    private final UserService userService;

    public void scrapContent(Integer contentId) {
        // 컨텐츠가 없는 경우
        Content content = contentService.getContentById(contentId)
                .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));

        // 컨텐츠가 있는 경우
        Users user = userService.getUser();

        // 기존에 스크랩한 항목인지 점검
        Optional<ContentScrap> contentScrapOptional =
                contentScrapRepository.findByUserAndContent(user, content);

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
    public List<ContentScrapDto> getContentScrapped() {
        Users user = userService.getUser();

        return contentScrapRepository.findAllByUser(user).stream()
                .map(ContentScrapDto::from)
                .toList();
    }

    /**
     * 스크랩된 콘텐츠 삭제
     */
    public void deleteContentScrapped(Integer contentScrapId) {
        boolean isExist = contentScrapRepository.existsById(contentScrapId);

        if (isExist) {
            contentScrapRepository.deleteById(contentScrapId);
        } else {
            throw new ScrapException(ErrorCode.SCRAP_NO_EXIST);
        }
    }

    /**
     * 용어 스크랩
     */
    public void scrapDictionary(Long dictionaryId) {
        // 스크랩하려는 용어가 존재하는 용어인지 확인
        Dictionary dictionary = dictionaryService.getDictionaryById(dictionaryId)
                .orElseThrow(() -> new DictionaryException(ErrorCode.DICTIONARY_NOT_EXIST));
        // 스크랩하려는 유저 데이터 획득
        Users user = userService.getUser();

        // 기존에 스크랩된 용어인지 확인
        Optional<DictionaryScrap> DictionaryScrapOptional = dictionaryScrapRepository.findByUserAndDictionary(user,
            dictionary);
        if (DictionaryScrapOptional.isEmpty()) { // 스크랩된 적 없는 경우
            DictionaryScrap newDictionaryScrap = DictionaryScrap.make(user, dictionary);
            dictionaryScrapRepository.save(newDictionaryScrap);
        } else { // 이미 스크랩된 경우
            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
        }
    }

    /**
     * 스크랩된 용어 전체 조회
     */
    public List<DictionaryScrapDto> getDictionaryScrapped() {

        Users user = userService.getUser();

        return dictionaryScrapRepository.findAllByUser(user).stream()
                .map(DictionaryScrapDto::from)
                .toList();
    }

    /**
     * 스크랩된 용어 삭제
     */
    public void deleteDictionaryScrapped(Long dictionaryScrapId) {
        boolean isExist = dictionaryScrapRepository.existsById(dictionaryScrapId);

        if (isExist) {
            dictionaryScrapRepository.deleteById(dictionaryScrapId);
        } else {
            throw new ScrapException(ErrorCode.SCRAP_NO_EXIST);
        }
    }
}
