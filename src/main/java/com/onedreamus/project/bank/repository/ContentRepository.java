package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import java.util.List;
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
}
