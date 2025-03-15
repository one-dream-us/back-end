package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query("SELECT n FROM News n WHERE (:cursor IS NULL OR n.id < :cursor) " +
        "ORDER BY n.id DESC")
    List<News> findByIdLessThanOrderByIdDesc(
        @Param("cursor") Integer cursor,
        Pageable pageable
    );

    Optional<News> findFirstByOrderByCreatedAtDesc();

    Page<News> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
