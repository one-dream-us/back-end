package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.dto.AgencySearch;
import com.onedreamus.project.thisismoney.model.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Integer> {

    List<Agency> findByNameContaining(String keyword);

    boolean existsByName(String agencyName);
}
