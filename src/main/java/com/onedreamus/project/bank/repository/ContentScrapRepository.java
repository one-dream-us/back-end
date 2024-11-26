package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.ContentScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentScrapRepository extends JpaRepository<ContentScrap, Integer> {

}
