package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Mission;
import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {

    Optional<Mission> findByUserAndDate(Users user, LocalDate now);

    List<Mission> findByUserAndDateBetween(Users user, LocalDate startDate, LocalDate endDate);
}
