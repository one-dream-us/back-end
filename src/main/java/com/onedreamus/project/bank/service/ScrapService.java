package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.exception.ScrapException;
import com.onedreamus.project.bank.exception.TermException;
import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.dto.ContentScrapDto;
import com.onedreamus.project.bank.model.dto.TermScrapDto;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.Term;
import com.onedreamus.project.bank.model.entity.TermScrap;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.ContentScrapRepository;
import com.onedreamus.project.bank.repository.TermScrapRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapService {

    private final TermScrapRepository termScrapRepository;
    private final ContentScrapRepository contentScrapRepository;
    private final ContentService contentService;
    private final TermService termService;
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
    public void scrapTerm(Integer termId) {
        // 스크랩하려는 용어가 존재하는 용어인지 확인
        Term term = termService.getTermById(termId)
                .orElseThrow(() -> new TermException(ErrorCode.TERM_NOT_EXIST));
        // 스크랩하려는 유저 데이터 획득
        Users user = userService.getUser();

        // 기존에 스크랩된 용어인지 확인
        Optional<TermScrap> termScrapOptional = termScrapRepository.findByUserAndTerm(user, term);
        if (termScrapOptional.isEmpty()) { // 스크랩된 적 없는 경우
            TermScrap newTermScrap = TermScrap.make(user, term);
            termScrapRepository.save(newTermScrap);
        } else { // 이미 스크랩된 경우
            throw new ScrapException(ErrorCode.ALREADY_SCRAPPED);
        }
    }

    /**
     * 스크랩된 용어 전체 조회
     */
    public List<TermScrapDto> getTermScrapped() {

        Users user = userService.getUser();

        return termScrapRepository.findAllByUser(user).stream()
                .map(TermScrapDto::from)
                .toList();
    }

    /**
     * 스크랩된 용어 삭제
     */
    public void deleteTermScrapped(Integer termScrapId) {
        boolean isExist = termScrapRepository.existsById(termScrapId);

        if (isExist) {
            termScrapRepository.deleteById(termScrapId);
        } else {
            throw new ScrapException(ErrorCode.SCRAP_NO_EXIST);
        }
    }
}
