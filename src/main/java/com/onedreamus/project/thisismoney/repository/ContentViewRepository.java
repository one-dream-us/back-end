package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentView;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Long> {
	@Query("SELECT SUM(cv.viewCount) FROM ContentView cv WHERE cv.content.id = :contentId")
	Integer findTotalViewCountByContentId(@Param("contentId") Long contentId);

	Optional<ContentView> findByContent(Content content);
}
