package com.onedreamus.project.thisismoney.service;

import com.onedreamus.project.thisismoney.model.dto.AgencySearch;
import com.onedreamus.project.thisismoney.repository.AgencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    public List<AgencySearch> searchAgency(String keyword) {
        return agencyRepository.findByNameContaining(keyword).stream()
                .map(AgencySearch::from)
                .toList();
    }
}
