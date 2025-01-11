package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.News;
import com.onedreamus.project.thisismoney.model.entity.NewsTag;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsTagRepository extends JpaRepository<NewsTag, Integer> {

    List<NewsTag> findByNews(News news);
}
