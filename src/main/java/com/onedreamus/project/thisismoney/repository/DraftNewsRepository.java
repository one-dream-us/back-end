package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DraftNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftNewsRepository extends JpaRepository<DraftNews, Integer> {
    Page<DraftNews> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
