package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Watch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Integer> {
}
