package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Integer> {
}