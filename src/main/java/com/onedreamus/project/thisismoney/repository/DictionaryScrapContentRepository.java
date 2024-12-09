package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.DictionaryScrapContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryScrapContentRepository extends JpaRepository<DictionaryScrapContent, Integer> {
}
