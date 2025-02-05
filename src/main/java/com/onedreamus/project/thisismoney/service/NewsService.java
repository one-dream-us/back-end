package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.exception.ErrorCode;
import com.onedreamus.project.global.util.NumberFormatter;
import com.onedreamus.project.global.util.UrlUtils;
import com.onedreamus.project.thisismoney.exception.NewsException;
import com.onedreamus.project.thisismoney.model.dto.*;
import com.onedreamus.project.thisismoney.model.entity.*;
import com.onedreamus.project.thisismoney.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsViewRepository newsViewRepository;
    private final SentenceRepository sentenceRepository;
    private final DictionarySentenceRepository dictionarySentenceRepository;
    private final UsersStudyDaysRepository usersStudyDaysRepository;

    public CursorResult<NewsListResponse> getNewList(Integer cursor, Integer size) {
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
        List<String> tags = getTags(news);

        Integer viewCount = newsViewRepository.findTotalViewCountByNews(news)
            .orElse(0);

        String formattedViewCount = NumberFormatter.format(viewCount);

        return NewsListResponse.from(news, formattedViewCount, tags);
    }

    private List<String> getTags(News news) {
        List<String> tags = new ArrayList<>();

        List<Sentence> sentences = sentenceRepository.findByNews(news);
        List<Dictionary> dictionaries =
                dictionarySentenceRepository.findDictionaryBySentenceIn(sentences);
        for (Dictionary dictionary : dictionaries) {
            tags.add(dictionary.getTerm()
            );
        }

        return tags;
    }


    /**
     * <p>로그인 유저의 news 상세페이지 조회</p>
     * 로그인 한 유저의 경우 학습 일수가 누적된다.
     * @param newsId
     * @param user
     * @return
     */
    @Transactional
    public NewsDetailResponse getNewsDetail(int newsId, Users user) {
        NewsDetailResponse newsDetailResponse = getNewsDetail(newsId);

        // 학습 일수 누적
        increaseStudyDay(user);
        return newsDetailResponse;
    }

    /**
     * <p>학습 일수 누적</p>
     * @param user
     */
    private void increaseStudyDay(Users user) {
        boolean isStudyDone = usersStudyDaysRepository.existsByUserAndStudyDate(user, LocalDate.now());
        if (!isStudyDone) {
            usersStudyDaysRepository.save(UsersStudyDays.builder()
                    .user(user)
                    .studyDate(LocalDate.now())
                    .build());
        }
    }

    /**
     * news 상세페이지 조회
     */
    @Transactional
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
            DictionarySentence dictionarySentences =
                dictionarySentenceRepository.findBySentence(sentence)
                    .orElse(new DictionarySentence());
            descriptionDtos.add(DictionaryDescriptionDto.from(sentence.getValue(),
                dictionarySentences.getDictionary()));
        }

        // 조회수 증가
        increaseView(news);

        return NewsDetailResponse.from(news, fullSentenceBuilder.toString(), descriptionDtos);
    }

    /**
     * <p>[최신 뉴스 콘텐츠 조회]</p>
     * 가장 최근에 올라온 뉴스 콘텐츠를 조회합니다.
     *
     * @return
     */
    public NewsListResponse getLatestNews() {
        News latestNews = newsRepository.findFirstByOrderByCreatedAtDesc()
            .orElseThrow(() -> new NewsException(ErrorCode.CONTENT_NOT_EXIST));

        int totalViewCnt = newsViewRepository.findTotalViewCountByNews(latestNews)
            .orElse(0);

        List<String> tags = getTags(latestNews);

        return NewsListResponse.from(latestNews, NumberFormatter.format(totalViewCnt), tags);
    }

    private void increaseView(News news) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59, 999999999);
        NewsView newsView =
                newsViewRepository.findByNewsAndViewDateBetween(news, startOfDay, endOfDay)
                        .orElse(NewsView.from(news));
        newsView.setViewCount(newsView.getViewCount() + 1);
        newsViewRepository.save(newsView);
    }

    public Optional<News> getNewsById(Integer newsId) {
        return newsRepository.findById(newsId);
    }
}
