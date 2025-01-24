package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Users;
import com.onedreamus.project.thisismoney.model.entity.UsersStudyDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UsersStudyDaysRepository extends JpaRepository<UsersStudyDays, Long> {
    int countByUser(Users user);

    boolean existsByUserAndStudyDate(Users user, LocalDate now);
}
