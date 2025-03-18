package com.onedreamus.project.thisismoney.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class NewsCacheScheduler {

    /**
     * 인기 뉴스 캐시 갱신
     * 매주 일요일 오후 11시 59분에 갱신
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    @CacheEvict(value = "popularNewsCache", allEntries = true)
    public void refreshPopularNewsCache() {
        log.info("Popular news cache has been refreshed at ({})", LocalDateTime.now());
    }
}
