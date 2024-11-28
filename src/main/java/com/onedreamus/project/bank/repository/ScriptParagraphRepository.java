package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ScriptParagraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptParagraphRepository extends JpaRepository<ScriptParagraph, Integer> {
}
