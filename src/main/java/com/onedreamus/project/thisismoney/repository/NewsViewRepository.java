package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsView;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsViewRepository extends JpaRepository<NewsView, Integer> {

    @Query("SELECT SUM(nv.viewCount) FROM NewsView nv WHERE nv.news = :news")
    Optional<Integer> findTotalViewCountByNews(@Param("news") News news);

    Optional<NewsView> findByNews(News news);

    @Query("SELECT nv FROM NewsView nv "
        + "WHERE nv.news = :news"
        + "AND DATE(nv.viewDate) = DATE(:today)")
    Optional<NewsView> findByNewsAndViewDateToday(@Param("news") News news, @Param("today") LocalDateTime today);
}
