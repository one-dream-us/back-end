package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.dto.DictionaryResponse;
import com.onedreamus.project.thisismoney.model.entity.Dictionary;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    List<Dictionary> findByIdIn(List<Long> randomNumList);

    @Query("select max(d.id) from Dictionary d")
    Long findMaxId();

    @Query("select d.id from Dictionary d")
    List<Long> findAllId();

    List<Dictionary> findByTermContaining(String keyword);
}
