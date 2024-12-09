package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentView;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Long> {
	@Query("SELECT SUM(cv.viewCount) FROM ContentView cv WHERE cv.content.id = :contentId")
	Integer findTotalViewCountByContentId(@Param("contentId") Long contentId);
	@Modifying
	@Query(value =
		"INSERT INTO content_view (content_id, view_count, view_date) " +
			"VALUES (:contentId, 1, CURRENT_TIMESTAMP) " +
			"ON CONFLICT (content_id) " +
			"DO UPDATE SET view_count = content_view.view_count + 1, " +
			"view_date = CURRENT_TIMESTAMP",
		nativeQuery = true)
	void upsertViewCount(@Param("contentId") Long contentId);
}
