package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.onedreamus.project.thisismoney.model.projection.TotalViewCountProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsViewRepository extends JpaRepository<NewsView, Integer> {

    @Query("SELECT SUM(nv.viewCount) FROM NewsView nv WHERE nv.news = :news")
    Optional<Integer> findTotalViewCountByNews(@Param("news") News news);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<NewsView> findByNewsAndViewDateBetween(News news, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT nv.news AS news, SUM(nv.viewCount) AS totalViewCount FROM NewsView nv " +
            "WHERE nv.viewDate > :beforeOneWeek " +
            "GROUP BY nv.news " +
            "ORDER BY totalViewCount DESC ")
    List<TotalViewCountProjection> findWeeklyTopNewsViews(Pageable pageable, @Param("beforeOneWeek") LocalDateTime beforeOneWeek);
}
