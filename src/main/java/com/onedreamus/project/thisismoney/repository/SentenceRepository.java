package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentenceRepository extends JpaRepository<Sentence, Integer> {

    List<Sentence> findByNews(News news);
}
