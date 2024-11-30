package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.UserTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTermsRepository extends JpaRepository<UserTerms, Integer> {
}
