package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Content;
import com.onedreamus.project.bank.model.entity.ContentScrap;
import com.onedreamus.project.bank.model.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentScrapRepository extends JpaRepository<ContentScrap, Integer> {

    List<ContentScrap> findAllByUser(Users user);

    Optional<ContentScrap> findByUserAndContent(Users user, Content content);
    @Query("SELECT COUNT(cs) FROM ContentScrap cs WHERE cs.content.id = :contentId")
    Integer countByContentId(@Param("contentId") Long contentId);
}
