package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsViewRepository extends JpaRepository<NewsView, Integer> {

    @Query("SELECT SUM(nv.viewCount) FROM NewsView nv WHERE nv.news = :news")
    Integer findTotalViewCountByNews(@Param("news") News news);
}
