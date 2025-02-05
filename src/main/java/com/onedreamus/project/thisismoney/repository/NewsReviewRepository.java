package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.NewsReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsReviewRepository extends JpaRepository<NewsReview, Long> {
}
