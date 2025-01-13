package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.NumberFormatter;
import com.onedreamus.project.global.util.UrlUtils;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsViewRepository newsViewRepository;
    private final NewsTagRepository newsTagRepository;
    private final SentenceRepository sentenceRepository;
    private final DictionarySentenceRepository dictionarySentenceRepository;

    public CursorResult<NewsListResponse> getNewList(int cursor, int size) {
        PageRequest pageRequest = PageRequest.of(0, size + 1);
        List<News> newsList = newsRepository.findByIdLessThanOrderByIdDesc(cursor, pageRequest);
        long totalElements = newsRepository.count();

        boolean hasNext = newsList.size() > size;
        if (hasNext) {
            newsList.remove(newsList.size() - 1);
        }

        List<NewsListResponse> responses = newsList.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());


        Long nextCursor = hasNext && !responses.isEmpty() ?
            responses.get(responses.size() - 1).getNewsId() : null;

        return CursorResult.<NewsListResponse>builder()
            .contents(responses)
            .hasNext(hasNext)
            .nextCursor(nextCursor)
            .totalElements(totalElements)
            .build();
    }

    private NewsListResponse convertToResponse(News news) {
        List<String> tags = newsTagRepository.findByNews(news)
            .stream()
            .map(newsTag -> newsTag.getTag().getValue())
            .collect(Collectors.toList());

        Integer viewCount = newsViewRepository.findTotalViewCountByNews(news)
                .orElse(0);
        String formattedViewCount = NumberFormatter.format(viewCount);

        return NewsListResponse.from(news, formattedViewCount, tags);
    }

    /**
     * news 상세페이지 조회
     */
    public NewsDetailResponse getNewsDetail(int newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        List<Sentence> sentences = sentenceRepository.findByNews(news);

        StringBuilder fullSentenceBuilder = new StringBuilder();
        for (Sentence sentence : sentences) {
            fullSentenceBuilder.append(sentence.getValue());
        }

        List<DictionaryDescriptionDto> descriptionDtos = new ArrayList<>();
        for (Sentence sentence : sentences) {
            DictionarySentence dictionarySentences = dictionarySentenceRepository.findBySentence(sentence)
                    .orElse(new DictionarySentence());
            descriptionDtos.add(DictionaryDescriptionDto.from(sentence.getValue(), dictionarySentences.getDictionary()));
        }

        return NewsDetailResponse.from(news, fullSentenceBuilder.toString(), descriptionDtos);
    }
}
