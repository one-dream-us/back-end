package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.DictionaryScrapContent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryScrapContentRepository extends JpaRepository<DictionaryScrapContent, Integer> {
}
