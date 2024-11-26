package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, Integer> {

}
