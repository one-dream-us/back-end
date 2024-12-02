package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ScriptFull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptFullRepository extends JpaRepository<ScriptFull, Integer> {
}
