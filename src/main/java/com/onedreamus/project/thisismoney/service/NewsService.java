package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.global.util.NumberFormatter;
import com.onedreamus.project.global.util.UrlUtils;
import com.onedreamus.project.thisismoney.model.dto.ContentListResponse;
import com.onedreamus.project.thisismoney.model.dto.CursorResult;
import com.onedreamus.project.thisismoney.model.dto.NewsListResponse;
import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.ScriptSummary;
import com.onedreamus.project.thisismoney.repository.NewsRepository;
import com.onedreamus.project.thisismoney.repository.NewsTagRepository;
import com.onedreamus.project.thisismoney.repository.NewsViewRepository;
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

        Integer viewCount = newsViewRepository.findTotalViewCountByNews(news);
        String formattedViewCount = NumberFormatter.format(viewCount != null ? viewCount : 0);

        return NewsListResponse.from(news, formattedViewCount, tags);
    }

}
