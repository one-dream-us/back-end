package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.exception.ContentException;
import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.repository.ContentRepository;
import com.onedreamus.project.global.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public Optional<Content> getContentById(Integer contentId){
        return contentRepository.findById(contentId);
    }

}
