package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.NewsRequest;
import com.onedreamus.project.thisismoney.model.dto.ScheduledNewsResponse;
import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import com.onedreamus.project.thisismoney.repository.ScheduledNewsRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledNewsService {

    private final ScheduledNewsRepository scheduledNewsRepository;

    /**
     * <p>예약 뉴스 콘텐츠 등록</p>
     * 매일 새벽 6시에 업로드 되도록 설정됨.
     */
    public void scheduleUploadNews(NewsRequest newsRequest, LocalDate scheduledAt) {
        scheduledNewsRepository.save(ScheduledNews.from(newsRequest, scheduledAt));
    }


    public Optional<ScheduledNews> getScheduledNewsByScheduledAt(LocalDate date) {
        return scheduledNewsRepository.findByScheduledAt(date);
    }

    public Page<ScheduledNewsResponse> getScheduledNewsList(Pageable pageable) {
        return scheduledNewsRepository.findAllByOrderByScheduledAtAsc(pageable)
                .map(ScheduledNewsResponse::from);
    }

    public void deleteScheduledNews(ScheduledNews scheduledNews) {
        scheduledNewsRepository.delete(scheduledNews);
    }
}
