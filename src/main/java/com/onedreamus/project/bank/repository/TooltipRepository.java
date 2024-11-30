package com.onedreamus.project.bank.repository;

import com.onedreamus.project.bank.model.entity.Tooltip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TooltipRepository extends JpaRepository<Tooltip, Integer> {
}
