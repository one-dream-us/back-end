package com.onedreamus.project.thisismoney.repository;

import com.onedreamus.project.thisismoney.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndProvider(String email, String provider);

}
