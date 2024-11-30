package com.onedreamus.project.bank.service;

import com.onedreamus.project.bank.model.entity.Term;
import com.onedreamus.project.bank.repository.TermRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public Optional<Term> getTermById(Integer termId){
        return termRepository.findById(termId);
    }

}
