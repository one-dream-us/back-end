package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.Users;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentScrapRepository extends JpaRepository<ContentScrap, Integer> {

    List<ContentScrap> findAllByUser(Users user);

    Optional<ContentScrap> findByUserAndContent(Users user, Content content);

    Integer countByUser(Users user);
}
