package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ContentHistory;
import com.onedreamus.project.bank.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentHistoryRepository extends JpaRepository<ContentHistory, Integer> {
	@Query("SELECT COUNT(DISTINCT ch.content.id) FROM ContentHistory ch " +
		"WHERE ch.user = :user AND ch.isWatch = true")
	Long countByUser(@Param("user") Users user);
}
