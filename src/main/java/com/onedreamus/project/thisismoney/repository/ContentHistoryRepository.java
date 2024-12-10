package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Content;
import com.onedreamus.project.thisismoney.model.entity.ContentHistory;
import com.onedreamus.project.thisismoney.model.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentHistoryRepository extends JpaRepository<ContentHistory, Integer> {
	@Query("SELECT COUNT(DISTINCT ch.content.id) FROM ContentHistory ch WHERE ch.user = :user")
	Long countByUser(@Param("user") Users user);

	boolean existsByUserAndContent(Users user, Content content);

	Long countByUserAndIsDeleted(Users user, boolean isDeleted);

	List<ContentHistory> findAllByUser(Users user);
}
