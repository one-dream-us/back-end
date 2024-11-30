package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.dto.TermScrapDto;
import com.onedreamus.project.bank.model.entity.Term;
import com.onedreamus.project.bank.model.entity.TermScrap;
import com.onedreamus.project.bank.model.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermScrapRepository extends JpaRepository<TermScrap, Integer> {

    List<TermScrap> findAllByUser(Users user);

    Optional<TermScrap> findByUserAndTerm(Users user, Term term);
}
