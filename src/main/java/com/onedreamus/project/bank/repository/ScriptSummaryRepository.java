package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ScriptSummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptSummaryRepository extends JpaRepository<ScriptSummary, Integer> {
	Optional<ScriptSummary> findByContentId(Long contentId);
}
