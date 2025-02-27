package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledNewsRepository extends JpaRepository<ScheduledNews, Integer> {

    Optional<ScheduledNews> findByScheduledAt(LocalDate now);

    Page<ScheduledNews> findAllByOrderByScheduledAtAsc(Pageable pageable);

    boolean existsByScheduledAt(LocalDate scheduledAt);

}
