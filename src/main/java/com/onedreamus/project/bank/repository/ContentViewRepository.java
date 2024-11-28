package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ContentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Integer> {
}
