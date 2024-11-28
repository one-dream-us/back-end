package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.exception.UserException;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.TermScrap;
import com.onedreamus.project.bank.model.entity.Users;
import com.onedreamus.project.bank.repository.ContentScrapRepository;
import com.onedreamus.project.bank.repository.TermScrapRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final TermScrapRepository termScrapRepository;
    private final ContentScrapRepository contentScrapRepository;
    private final ContentService contentService;
    private final UserService userService;

    public void scrapContent(Integer contentId) {
        // 컨텐츠가 없는 경우
        Content content = contentService.getContentById(contentId)
            .orElseThrow(() -> new ContentException(ErrorCode.CONTENT_NOT_EXIST));


        // 컨텐츠가 있는 경우
        String email = SecurityUtils.getEmail();
        Users user = userService.getUserByEmail(email)
            .orElseThrow(() -> new UserException(ErrorCode.NO_USER));

        ContentScrap newContentScrap = ContentScrap.builder()
            .user(user)
            .content(content)
            .build();

        contentScrapRepository.save(newContentScrap);
    }

}
