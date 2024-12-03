package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
	@Query("SELECT c FROM Content c WHERE (:cursor IS NULL OR c.id < :cursor) " +
		"ORDER BY c.id DESC")
	List<Content> findByIdLessThanOrderByIdDesc(
		@Param("cursor") Long cursor,
		Pageable pageable
	);

	@Query("SELECT COUNT(c) FROM Content c")
	long countTotalContents();

	Optional<Content> findTopByOrderByCreatedAtDesc();

	@Query("SELECT c FROM Content c " +
		"LEFT JOIN ContentView cv ON c.id = cv.content.id " +
		"GROUP BY c " +
		"ORDER BY SUM(cv.viewCount) DESC NULLS LAST")
	List<Content> findTop5ByOrderByViewCountDesc(Pageable pageable);

	default List<Content> findTop5ByOrderByViewCountDesc() {
		return findTop5ByOrderByViewCountDesc(PageRequest.of(0, 5));
	}
}
