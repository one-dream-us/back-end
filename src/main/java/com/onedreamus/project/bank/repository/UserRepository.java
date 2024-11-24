package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
