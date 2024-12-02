package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
}
