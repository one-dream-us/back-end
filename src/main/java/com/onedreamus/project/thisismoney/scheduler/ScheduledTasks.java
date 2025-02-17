package com.onedreamus.project.thisismoney.scheduler;

import com.onedreamus.project.thisismoney.model.dto.ScheduledNewsRequest;
import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import com.onedreamus.project.thisismoney.service.NewsService;
import com.onedreamus.project.thisismoney.service.ScheduledNewsService;

import java.time.LocalDate;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final NewsService newsService;
    private final ScheduledNewsService scheduledNewsService;

    @Scheduled(cron = "0 0 6 * * ?")
    public void uploadScheduledNews() {
        LocalDate now = LocalDate.now();
        Optional<ScheduledNews> scheduledNewsOptional =
                scheduledNewsService.getScheduledNewsByScheduledAt(now);
        if (scheduledNewsOptional.isEmpty()) {
            log.info("[{} : 오늘 예약된 업로드 예정 뉴스 콘텐츠가 없습니다.]", now);
            return;
        }

        ScheduledNews scheduledNews = scheduledNewsOptional.get();
        ScheduledNewsRequest scheduledNewsRequest = scheduledNews.getScheduledNewsRequest();

        newsService.uploadScheduledNews(
                scheduledNewsRequest,
                scheduledNewsRequest.getDictionarySentenceList());

        log.info("[{} : 뉴스 콘테츠 업로드 완료]", now);
        scheduledNewsService.deleteScheduledNews(scheduledNews);
    }
}
