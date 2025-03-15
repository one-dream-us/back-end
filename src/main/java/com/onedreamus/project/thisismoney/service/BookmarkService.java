package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.repository.DictionaryBookmarkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private DictionaryBookmarkRepository dictionaryBookmarkRepository;
}
