package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.ScheduledNews;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledNewsRepository extends JpaRepository<ScheduledNews, Integer> {

    Optional<ScheduledNews> findByScheduledAt(LocalDate now);

}
