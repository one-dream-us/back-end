package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Dictionary;
import com.onedreamus.project.bank.model.entity.DictionaryScrap;
import com.onedreamus.project.bank.model.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryScrapRepository extends JpaRepository<DictionaryScrap, Long> {

    List<DictionaryScrap> findAllByUser(Users user);

    Optional<DictionaryScrap> findByUserAndDictionary(Users user, Dictionary dictionary);
}
